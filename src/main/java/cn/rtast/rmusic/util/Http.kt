/*
 * Copyright © 2024 RTAkland
 * Author: RTAkland
 * Date: 2024/12/1
 */

//@file:Suppress("unused")

package cn.rtast.rmusic.util


import cn.rtast.rmusic.RMusic
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * 封装好的Http工具类, 包含了大部分的http方法
 * 需要http请求来发送消息/操作的模块可以引入
 */
object Http {

    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    val jsonHeader = mapOf(
        "Content-Type" to "application/json; charset=utf-8",
        "Accept" to "application/json"
    )
    val okHttpClient = OkHttpClient()

    fun buildParams(url: String, params: Map<String, Any>?): String {
        val paramsUrl = StringBuilder("$url?")
        params?.let {
            it.forEach { (key, value) ->
                paramsUrl.append("$key=$value&")
            }
            RMusic.loginManager.getCookie()?.let {
                paramsUrl.append("cookie=$it&")
            }
            paramsUrl.dropLast(1)
        }
        println(paramsUrl)
        return if (params != null) paramsUrl.toString() else url
    }

    fun addHeaders(request: Request.Builder, headers: Map<String, String>?): Request.Builder {
        headers?.let {
            it.forEach { (key, value) ->
                request.addHeader(key, value)
            }
        }
        return request
    }

    inline fun <reified T> executeRequest(request: Request): T {
        val result = okHttpClient.newCall(request).execute().body.string()
        return result.fromJson<T>()
    }

    fun executeRequest(request: Request): String {
        return okHttpClient.newCall(request).execute().body.string()
    }

    @JvmOverloads
    fun get(
        url: String,
        params: Map<String, Any>? = null,
        headers: Map<String, String>? = null,
    ): String {
        val paramsUrl = buildParams(url, params)
        val request = Request.Builder()
            .url(paramsUrl)
            .get()
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build())
    }

    @JvmOverloads
    inline fun <reified T> get(
        url: String,
        params: Map<String, Any>? = null,
        headers: Map<String, String>? = null,
    ): T {
        return get(url, params, headers).fromJson<T>()
    }

    @JvmOverloads
    inline fun <reified T> post(
        url: String,
        formBody: Map<String, Any>? = null,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): T {
        val body = FormBody.Builder()
        val paramsUrl = buildParams(url, params)
        formBody?.let {
            it.forEach { (key, value) ->
                body.add(key, value.toString())
            }
        }
        val request = Request.Builder()
            .post(body.build())
            .url(paramsUrl)
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest<T>(headerRequest.build())
    }

    @JvmOverloads
    inline fun <reified T> post(
        url: String,
        jsonBody: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): T {
        val result = post(url, jsonBody, headers, params)
        return result.fromJson<T>()
    }

    @JvmOverloads
    fun post(
        url: String,
        jsonBody: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): String {
        val body = jsonBody.toRequestBody(jsonMediaType)
        val paramsUrl = buildParams(url, params)
        val request = Request.Builder()
            .post(body)
            .url(paramsUrl)
        this.addHeaders(request, jsonHeader)
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build())
    }

    @JvmOverloads
    inline fun <reified T> post(
        url: String,
        form: FormBody,
        headers: Map<String, String>? = null,
    ): T {
        val request = Request.Builder()
            .post(form)
            .url(buildParams(url, headers))
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build()).fromJson<T>()
    }

    @JvmOverloads
    inline fun <reified T> post(
        url: String,
        form: MultipartBody,
        headers: Map<String, String>? = null,
    ): T {
        val request = Request.Builder()
            .post(form)
            .url(buildParams(url, headers))
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build()).fromJson<T>()
    }

    inline fun <reified T> put(
        url: String,
        jsonBody: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): T {
        val paramsUrl = buildParams(url, params)
        val request = Request.Builder()
            .url(paramsUrl)
            .put(jsonBody.toRequestBody(jsonMediaType))
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build()).fromJson<T>()
    }

    fun put(
        url: String,
        jsonBody: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): String {
        val paramsUrl = buildParams(url, params)
        val request = Request.Builder()
            .url(paramsUrl)
            .put(jsonBody.toRequestBody(jsonMediaType))
        val headerRequest = addHeaders(request, headers)
        return this.executeRequest(headerRequest.build())
    }

    fun buildDeleteRequest(
        url: String,
        jsonBody: String? = null,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): Request {
        val paramsUrl = buildParams(url, params)
        val requestBuilder = Request.Builder()
            .url(paramsUrl)

        if (jsonBody != null) {
            requestBuilder.delete(jsonBody.toRequestBody(jsonMediaType))
        } else {
            requestBuilder.delete()
        }

        return addHeaders(requestBuilder, headers).build()
    }

    @JvmOverloads
    inline fun <reified T> delete(
        url: String,
        jsonBody: String? = null,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): T {
        val request = buildDeleteRequest(url, jsonBody, headers, params)
        return executeRequest(request).fromJson<T>()
    }

    @JvmOverloads
    fun delete(
        url: String,
        jsonBody: String? = null,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
    ): String {
        val request = buildDeleteRequest(url, jsonBody, headers, params)
        return executeRequest(request)
    }
}
