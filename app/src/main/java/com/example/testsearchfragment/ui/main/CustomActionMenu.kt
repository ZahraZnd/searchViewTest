package com.example.testsearchfragment.ui.main

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.view.ActionMode
import com.example.testsearchfragment.R


class CustomActionMenu(bV: TextView) : ActionMode.Callback,
    android.view.ActionMode.Callback {

    val bodyView = bV


    override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
        Log.d(TAG, "onCreateActionMode")
//        menu?.clear()
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.textclick, menu)
        return true
    }

    override fun onPrepareActionMode(p0: android.view.ActionMode?, menu: Menu?): Boolean {
//        menu?.removeItem(android.R.id.selectAll)
//        // Remove the "cut" option
//        menu?.removeItem(android.R.id.cut)
//        // Remove the "copy all" option
//        menu?.removeItem(android.R.id.copy)
//        // Remove the "dictionary" option

        menu?.add(R.id.dictinory)
        menu?.add(R.id.terminology)
        // Remove the "shared" option
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            menu?.removeItem(android.R.id.shareText)
//        }
        return false
    }

//    override fun onActionItemClicked(p0: android.view.ActionMode?, item: MenuItem?): Boolean {
//        Log.d(
//            TAG,
//            String.format("onActionItemClicked item=%s/%d", item.toString(), item?.itemId)
//        )
//        val cs: CharacterStyle
//        val start = bodyView.selectionStart
//        val end = bodyView.selectionEnd
//        val ssb = SpannableStringBuilder(bodyView.text)
//
//        when (item?.itemId) {
//
//            R.id.italic -> {
//                cs = StyleSpan(Typeface.ITALIC)
//                ssb.setSpan(cs, start, end, 1)
//                bodyView.text = ssb
//                return true
//            }
//
//        }
//        return false
//    }

    override fun onActionItemClicked (p0: android.view.ActionMode?, item: MenuItem?): Boolean {
        Log.d("IDZAHRA","${item?.itemId}")
        return true
    }

    override fun onDestroyActionMode(p0: android.view.ActionMode?) = Unit


    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        Log.d(TAG, "onCreateActionMode")
        val inflater = mode.menuInflater
        inflater.inflate(R.menu.textclick, menu)
        menu.removeItem(android.R.id.selectAll)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        Log.d(
            TAG,
            String.format("onActionItemClicked item=%s/%d", item.toString(), item.itemId)
        )
        val cs: CharacterStyle
        val start = bodyView.selectionStart
        val end = bodyView.selectionEnd
        val ssb = SpannableStringBuilder(bodyView.text)

        when (item.itemId) {

            R.id.italic -> {
                cs = StyleSpan(Typeface.ITALIC)
                ssb.setSpan(cs, start, end, 1)
                bodyView.text = ssb
                return true
            }


        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode) = Unit
}