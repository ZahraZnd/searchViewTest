package com.example.testsearchfragment.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.example.testsearchfragment.R
import kotlinx.android.synthetic.main.blank_fragment.*

class BlankFragment : Fragment() {

    private lateinit var viewModel: BlankViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.blank_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs: BlankFragmentArgs by navArgs()
        val text = safeArgs.tvText
        tv_arg.text = text

        tv_arg.customSelectionActionModeCallback = CustomActionMenu(tv_arg)


        viewModel = ViewModelProviders.of(this).get(BlankViewModel::class.java)
    }

}
