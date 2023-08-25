package com.media.gallery.domain.extensions

import android.database.Cursor
import java.io.File

fun File.isMediaFile() = absolutePath.isMediaFile()

fun Cursor.getStringValue(key: String): String {
    return getString(getColumnIndexOrThrow(key))
}

fun Cursor.getStringValueOrNull(key: String) =
    if (isNull(getColumnIndexOrThrow(key))) null else getString(getColumnIndexOrThrow(key))

fun Cursor.getIntValue(key: String) = getInt(getColumnIndexOrThrow(key))

fun Cursor.getIntValueOrNull(key: String) =
    if (isNull(getColumnIndexOrThrow(key))) null else getInt(getColumnIndexOrThrow(key))

fun Cursor.getLongValue(key: String) = getLong(getColumnIndexOrThrow(key))

fun Cursor.getLongValueOrNull(key: String) =
    if (isNull(getColumnIndexOrThrow(key))) null else getLong(getColumnIndexOrThrow(key))

fun Cursor.getBlobValue(key: String): ByteArray = getBlob(getColumnIndexOrThrow(key))
