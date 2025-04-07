package com.gagik.drugsreminder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gagik.drugsreminder.R
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.common.repository.Repository
import com.gagik.drugsreminder.databinding.FragmentDrugsListBinding
import com.gagik.drugsreminder.ui.adapters.DrugsAdapter
import com.gagik.drugsreminder.ui.dialog.DrugsDetailsDialog
import com.gagik.drugsreminder.ui.viewModels.DrugsListViewModel
import com.gagik.drugsreminder.ui.viewModels.RepositoryViewModelFactory
import com.gagik.drugsreminder.ui.viewModels.SharedViewModel
import kotlinx.coroutines.launch


class DrugsListFragment : Fragment(), DrugsListeners {

    companion object {
        fun newInstance(): DrugsListFragment {
            return DrugsListFragment()
        }
    }

    private val repository: Repository by lazy {
        Repository.getRepository(requireContext())
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: DrugsListViewModel by viewModels {
        RepositoryViewModelFactory(repository)
    }

    private var _binding: FragmentDrugsListBinding? = null
    private val binding get() = _binding!!
    private val adapter = DrugsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrugsListBinding.inflate(layoutInflater, container, false)
        binding.drugsRcv.adapter = adapter
        viewModel.getAllDrugs {
            adapter.submitOriginalList(it)
        }
        setupSearchQuery()
        clickListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDragDetailsClick(drug: DrugsEntity) {
        val drugsDetailsDialog = DrugsDetailsDialog.newInstance(drug.id)
        drugsDetailsDialog.show(childFragmentManager, "DrugsDetailsDialog")
    }

    override fun onDragItemClick(drug: DrugsEntity) {
        sharedViewModel.selectedDrug.value = drug
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDeleteItem(drug: DrugsEntity) {
//        repository.deleteDrugById(drug.id)
//        val currentList = adapter.currentList
//        currentList.remove(drug)
//        adapter.submitOriginalList(currentList)

//        val coroutineScope = CoroutineScope(Dispatchers.Default) // Use Default dispatcher for background work
//        val coroutine = coroutineScope.launch {
//            repository.deleteDrugById(drug.id)
//        }

        lifecycleScope.launch {
            repository.deleteDrugById(drug.id)
        }

//        // Observe coroutine completion on the main thread
//        coroutine.invokeOnCompletion {
//            if (it == null) {
//                val currentList = adapter.currentList.toMutableList()
//                currentList.remove(drug)
//                adapter.submitOriginalList(currentList)
//            } else {
//                Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun clickListeners() {
        binding.addNewDrugBtn.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, AddNewDrugFragment.newInstance())
                .commit()
        }

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupSearchQuery() {
        binding.drugsSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filterList(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

}

interface DrugsListeners {
    fun onDragDetailsClick(drug: DrugsEntity)
    fun onDragItemClick(drug: DrugsEntity)
    fun onDeleteItem(drug: DrugsEntity)
}
