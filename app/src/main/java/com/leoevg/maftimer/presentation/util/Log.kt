package com.leoevg.maftimer.presentation.util


import android.util.Log

object Logx {
    fun success(tag: String, message: String) = Log.i(tag, "✅ $message")
    fun info(tag: String, message: String) = Log.i(tag, "ℹ️ $message")
    fun action(tag: String, message: String) = Log.d(tag, "🚀 $message")
    fun storage(tag: String, message: String) = Log.d(tag, "💾 $message")
    fun network(tag: String, message: String) = Log.d(tag, "🌐 $message")
    fun warn(tag: String, message: String) = Log.w(tag, "⚠️ $message")
    fun debug(tag: String, message: String) = Log.d(tag, "🔧 $message")
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable == null) Log.e(tag, "❌ $message") else Log.e(tag, "❌ $message", throwable)
    }
}