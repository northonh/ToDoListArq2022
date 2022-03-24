package br.edu.ifsp.scl.sdm.pa2.todolistarq.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.database.ToDoListArqDatabase
import br.edu.ifsp.scl.sdm.pa2.todolistarq.model.entity.Tarefa
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TarefaViewModel(application: Application): AndroidViewModel(application) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(
            application.baseContext,
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }
    private val listaTarefasMld: MutableLiveData<MutableList<Tarefa>> = MutableLiveData()
    private val tarefaMld: MutableLiveData<Tarefa> = MutableLiveData()

    // Funções para recuperar os observáveis
    fun recuperarListaTarefas() = listaTarefasMld
    fun recuperarTarefa() = tarefaMld

    // Funções de acesso ao data source
    fun atualizaTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().atualizarTarefa(tarefa)
            tarefaMld.postValue(tarefa)
        }
    }

    fun insereTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            val id = database.getTarefaDao().inserirTarefa(tarefa)
            tarefaMld.postValue(
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
            listaTarefasMld.postValue(listaTarefas.toMutableList())
        }
    }

    fun removeTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().removerTarefa(tarefa)
        }
    }

}