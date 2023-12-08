package com.jdccmobile.costofliving.ui.home.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jdccmobile.costofliving.R


class DetailsFragment : Fragment() {

    private val safeArgs: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("DetailsFragment", "onCreateView safeargs: ${safeArgs.place}")
        return inflater.inflate(R.layout.fragment_details, container, false)
    }



}