package com.gagik.drugsreminder.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.common.repository.Repository
import com.gagik.drugsreminder.databinding.FragmentAddNewDrugBinding
import com.gagik.drugsreminder.ui.viewModels.AddNewDrugViewModel
import com.gagik.drugsreminder.ui.viewModels.RepositoryViewModelFactory

class AddNewDrugFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewDrugFragment()
    }

    private var _binding: FragmentAddNewDrugBinding? = null
    private val binding get() = _binding!!
    private val repository: Repository by lazy {
        Repository.getRepository(requireContext())
    }
    private val viewModel: AddNewDrugViewModel by viewModels {
        RepositoryViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewDrugBinding.inflate(inflater, container, false)
        clickListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickListeners() {
        binding.saveBtn.setOnClickListener {
            if (binding.nameEt.text.isNullOrEmpty()) {
                binding.nameEt.error = "Name is required"
                return@setOnClickListener
            }

            binding.nameEt.error = null
            val drugsEntity = DrugsEntity(
                name = binding.nameEt.text.nullOrText(),
                activeFactor = binding.activeFactorEt.text.nullOrText(),
                amountSubstance = binding.amountSubstanceEt.text.nullOrText(),
                contraindications = binding.contraindicationsEt.text.nullOrText(),
                sideEffects = binding.sideEffectsEt.text.nullOrText(),
            )
            viewModel.saveDrug(drugsEntity) {
                Toast.makeText(requireContext(), "Drug saved", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}

private fun Editable?.nullOrText() = if (isNullOrEmpty()) null else toString().trim()