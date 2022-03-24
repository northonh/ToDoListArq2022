package br.edu.ifsp.scl.sdm.pa2.todolistarq.presenter

import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import br.edu.ifsp.scl.sdm.pa2.todolistarq.view.BaseFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TarefaPresenter(private val baseFragment: BaseFragment) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(
            baseFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    fun atualizaTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().atualizarTarefa(tarefa)
            baseFragment.retornaTarefa(tarefa)
        }
    }

    fun insereTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = database.getTarefaDao().inserirTarefa(tarefa)
            baseFragment.retornaTarefa(
                Tarefa(
                    id.toInt(),
                    tarefa.nome,
                    tarefa.realizada
                )
            )
        }
    }

    fun buscarTarefas() {
        GlobalScope.launch {
            val listaTarefas = database.getTarefaDao().recuperarTarefas()
            baseFragment.atualizarListaTarefas(listaTarefas.toMutableList())
        }
    }

    fun removeTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().removerTarefa(tarefa)
        }
    }

    interface TarefaView {
        fun atualizarListaTarefas(listaTarefas: MutableList<Tarefa>)
        fun retornaTarefa(tarefa: Tarefa)
    }
}