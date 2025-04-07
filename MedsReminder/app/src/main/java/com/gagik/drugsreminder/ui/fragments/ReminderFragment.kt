package com.gagik.drugsreminder.ui.fragments

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.gagik.drugsreminder.R
import com.gagik.drugsreminder.alarmManager.DrugAlarmManager
import com.gagik.drugsreminder.databinding.FragmentReminderBinding
import com.gagik.drugsreminder.ui.viewModels.ReminderViewModel
import com.gagik.drugsreminder.ui.viewModels.ReminderViewModelFactory
import com.gagik.drugsreminder.ui.viewModels.SharedViewModel
import com.gagik.drugsreminder.utils.ResourceHelper


class ReminderFragment : Fragment(), ResourceHelper {
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private val alarmManager by lazy { DrugAlarmManager(requireContext()) }
    private val viewModel: ReminderViewModel by viewModels {
        ReminderViewModelFactory(alarmManager, this)
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            viewModel.onTimeSelected(hourOfDay, minute)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickListeners()
        observers()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickListeners() {
        binding.pickTimeButton.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                timePickerDialogListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        binding.selectDrugBtn.setOnClickListener {
            selectDrug()
        }

        binding.setAlarmBtn.setOnClickListener {
            viewModel.setAlarm { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.weeksGroup.setOnCheckedStateChangeListener { chipGroup, _ ->
            when (chipGroup.checkedChipId) {
                R.id.monday -> viewModel.setWeekDay(Calendar.MONDAY)
                R.id.tuesday -> viewModel.setWeekDay(Calendar.TUESDAY)
                R.id.wednesday -> viewModel.setWeekDay(Calendar.WEDNESDAY)
                R.id.thursday -> viewModel.setWeekDay(Calendar.THURSDAY)
                R.id.friday -> viewModel.setWeekDay(Calendar.FRIDAY)
                R.id.saturday -> viewModel.setWeekDay(Calendar.SATURDAY)
                R.id.sunday -> viewModel.setWeekDay(Calendar.SUNDAY)
                else -> viewModel.setWeekDay(Calendar.MONDAY)
            }
        }
    }

    private fun observers() {
        handleSelectedDrug()
    }

    private fun handleSelectedDrug() {
        sharedViewModel.selectedDrug.observe(viewLifecycleOwner) { drug ->
            viewModel.onDrugSelected(drug)
        }
    }

    private fun selectDrug() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, DrugsListFragment.newInstance())
            .commit()
    }

    companion object {
        fun newInstance(): ReminderFragment {
            return ReminderFragment()
        }

//        fun newInstanceWithArgs(name: String?): ReminderFragment {
//            val args = Bundle()
//            args.putString(REMINDER_ARGS_KEY, name)
//            val fragment = ReminderFragment()
//            fragment.arguments = args
//            return fragment
//        }
    }

}