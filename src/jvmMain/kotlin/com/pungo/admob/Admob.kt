package com.pungo.admob

import com.pungo.admob.Admob
import com.pungo.admob.AdmobCreateDefault
import com.soywiz.korge.view.Views

actual suspend fun AdmobCreate(views: Views, testing: Boolean): Admob = AdmobCreateDefault(views, testing)
