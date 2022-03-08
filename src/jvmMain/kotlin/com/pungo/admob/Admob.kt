package com.pungo.admob

import com.soywiz.korge.view.Views

actual suspend fun AdmobCreate(views: Views, testing: Boolean): Admob = AdmobCreateDefault(views, testing)
