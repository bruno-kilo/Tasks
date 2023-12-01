package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.utils.Navigator
import com.devmasterteam.tasks.viewmodel.RegisterViewModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener {

    private var viewModel: TaskFormViewModel? = null
    private var binding: ActivityTaskFormBinding? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = mutableListOf()
    private var taskIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        // Layout
        setContentView(binding?.root)

        initialize()
    }

    private fun initialize() {
        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Eventos
        setupClick()

        viewModel?.loadPriorities()
        loadDataFromActivity()

        observe()

    }

    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
            val dueDate = dateFormat.format(time)
            binding?.buttonDate?.text = dueDate
        }
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel?.load(taskIdentification)
        }
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (l in listPriority) {
            if (l.id == priorityId) {
                break
            }
            index++
        }
        return index
    }

    private fun observe() {
        viewModel?.priorityList?.observe(this) {
            listPriority = it
            val list = mutableListOf<String>()
            for (p in it) {
                list.add(p.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding?.spinnerPriority?.adapter = adapter
        }

        viewModel?.taskSave?.observe(this) {
            if (it.status()) {

                if (taskIdentification == 0) {
                    toast(R.string.task_created.toString())
                } else {
                    toast(R.string.task_updated.toString())
                }
                finish()
            } else {
                toast(it.message())
            }
        }

        viewModel?.task?.observe(this) {
            binding?.apply {
                editDescription.setText(it.description)
                spinnerPriority.setSelection(getIndex(it.priorityId))
                checkComplete.isChecked = it.complete
            }

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding?.buttonDate?.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        }

        viewModel?.taskLoad?.observe(this) {
            if (!it.status()) {
                toast(it.message())
                finish()
            }
        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    /*
           let
           - não retorna valor
           - uma cópia da variável

           apply
           - retorna valor
           - entra na variável
    */
    private fun handleSave() {
        binding?.apply {
            val task = TaskModel(
                id = taskIdentification,
                description = editDescription.text.toString(),
                complete = checkComplete.isChecked,
                dueDate = buttonDate.text.toString(),
                priorityId = listPriority.get(spinnerPriority.selectedItemPosition).id
            )
            viewModel?.save(task)
        }
    }

    private fun handleDate() {
        Calendar.getInstance().apply {
            val year = get(Calendar.YEAR)
            val month = get(Calendar.MONTH)
            val day = get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(this@TaskFormActivity, this@TaskFormActivity, year, month, day)
            // Essa linha eu implementei pra permitir somente datas a partir da data do dia
            datePickerDialog.datePicker.minDate = timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setupClick() {
        binding?.apply {
            buttonSave.setOnClickListener { handleSave() }
            buttonDate.setOnClickListener { handleDate() }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, TaskFormActivity::class.java)
    }
}


