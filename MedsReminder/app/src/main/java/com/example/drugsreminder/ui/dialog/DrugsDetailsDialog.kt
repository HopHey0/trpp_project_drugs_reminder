package com.gagik.drugsreminder.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.gagik.drugsreminder.common.repository.Repository
import com.gagik.drugsreminder.databinding.DrugsDetailsDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DrugsDetailsDialog: DialogFragment() {
    companion object {
        private const val DRUGS_KEY = "DRUGS_DETAILS_KEY"

        fun newInstance(id: Long): DrugsDetailsDialog{
            val args = Bundle().apply {
                putLong(DRUGS_KEY, id)
            }
            return DrugsDetailsDialog().apply {
                arguments = args
            }
        }
    }

    private var _binding: DrugsDetailsDialogBinding? = null
    private val binding get() = _binding!!
    private val repository: Repository by lazy {
        Repository.getRepository(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DrugsDetailsDialogBinding.inflate(layoutInflater)

        val drugId: Long = arguments?.getLong(DRUGS_KEY) ?: 0
        initializeDrugsProps(drugId)

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeDrugsProps(drugId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.getDrugById(drugId).collect { drug ->
                withContext(Dispatchers.Main) {
                    binding.nameTxt.text = drug.name
                    binding.activeFactorTxt.text = drug.activeFactor
                    binding.amountSubstanceTxt.text = drug.amountSubstance
                    binding.contraindicationsTxt.text = drug.contraindications
                    binding.sideEffectsTxt.text = drug.sideEffects
                }
            }
        }
    }

}