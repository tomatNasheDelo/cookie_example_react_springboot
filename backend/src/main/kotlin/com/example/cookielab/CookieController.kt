package com.example.cookielab

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cookies")
class CookieController(
    @Value("\${app.cookie.secure}") private val secure: Boolean,
    @Value("\${app.cookie.same-site}") private val sameSite: String,
) {
    @PostMapping("/set")
    fun setCookies(): ResponseEntity<CookieActionResponse> {
        val visible = cookie("demoVisible", "hello-from-browser-cookie", httpOnly = false).build()
        val hidden = cookie("demoHttpOnly", "server-only-demo-value", httpOnly = true).build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, visible.toString())
            .header(HttpHeaders.SET_COOKIE, hidden.toString())
            .body(CookieActionResponse("Установлены demoVisible и demoHttpOnly."))
    }

    @GetMapping("/inspect")
    fun inspect(request: HttpServletRequest): CookieInspectionResponse {
        val received = request.cookies.orEmpty().associate { it.name to it.value }
        return CookieInspectionResponse(
            receivedCookies = received.map { (name, value) ->
                ReceivedCookie(name, value, httpOnly = name == "demoHttpOnly")
            }.sortedBy { it.name },
            note = "Учебный endpoint: не возвращайте значения настоящих секретных cookie из API.",
        )
    }

    @PostMapping("/clear")
    fun clearCookies(): ResponseEntity<CookieActionResponse> {
        val visible = cookie("demoVisible", "", httpOnly = false).maxAge(0).build()
        val hidden = cookie("demoHttpOnly", "", httpOnly = true).maxAge(0).build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, visible.toString())
            .header(HttpHeaders.SET_COOKIE, hidden.toString())
            .body(CookieActionResponse("Обе demo-cookie удалены."))
    }

    private fun cookie(name: String, value: String, httpOnly: Boolean): ResponseCookie.ResponseCookieBuilder =
        ResponseCookie.from(name, value)
            .httpOnly(httpOnly)
            .secure(secure)
            .path("/")
            .sameSite(sameSite)
            .maxAge(3600)
}

data class CookieActionResponse(val message: String)
data class CookieInspectionResponse(val receivedCookies: List<ReceivedCookie>, val note: String)
data class ReceivedCookie(val name: String, val value: String, val httpOnly: Boolean)
