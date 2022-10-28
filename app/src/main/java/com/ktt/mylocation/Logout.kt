package com.ktt.mylocation

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ktt.mylocation.databinding.FragmentLogoutBinding

class Logout : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLogoutBinding
    private lateinit var pDialogue: ProgressDialog
    private lateinit var loginDataStore: LoginDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLogoutBinding.inflate(inflater, container, false)

        loginDataStore = LoginDataStore(requireContext())

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {

            lifecycleScope.launchWhenStarted {
                loginDataStore.clearValues()
                dialog?.dismiss()
                requireActivity().finish()
                requireContext().startActivity(Intent(requireContext(), Login::class.java))
            }

        }

        binding.cancelButton.setOnClickListener {

            dialog?.dismiss()

        }
    }

}