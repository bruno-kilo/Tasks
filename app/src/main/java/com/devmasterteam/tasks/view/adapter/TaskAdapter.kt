package com.devmasterteam.tasks.view.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import java.text.SimpleDateFormat

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var listTasks: List<TaskModel> = arrayListOf()
    private lateinit var listener: TaskListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowTaskListBinding.inflate(inflater, parent, false)
        return TaskViewHolder(itemBinding, listener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    override fun getItemCount(): Int {
        return listTasks.count()
    }

    fun updateTasks(list: List<TaskModel>) {
        listTasks = list
        notifyDataSetChanged()
    }

    fun attachListener(taskListener: TaskListener) {
        listener = taskListener
    }

    class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(task: TaskModel) {
            //Esse itemBinding ficou lindo refatorado!
            itemBinding.apply {
                textDescription.text = task.description
                textPriority.text = task.priorityDescription
                val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate)
                textDueDate.text = SimpleDateFormat("yyyy-MM-dd").format(date)

                if (task.complete) {
                    imageTask.setImageResource(R.drawable.ic_done)
                } else {
                    imageTask.setImageResource(R.drawable.ic_todo)
                }

                // Eventos
                textDescription.setOnClickListener { listener.onListClick(task.id) }
                imageTask.setOnClickListener {
                    if (task.complete) {
                        listener.onUndoClick(task.id)
                    } else {
                        listener.onCompleteClick(task.id)
                    }
                }
                textDescription.setOnLongClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle(R.string.remocao_de_tarefa)
                        .setMessage(R.string.remover_tarefa)
                        .setPositiveButton(R.string.sim) { dialog, which ->
                            listener.onDeleteClick(task.id)
                        }
                        .setNeutralButton(R.string.cancelar, null)
                        .show()
                    true
                }
            }
        }
    }

}