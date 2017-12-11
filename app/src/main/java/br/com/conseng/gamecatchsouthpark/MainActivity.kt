package br.com.conseng.gamecatchsouthpark

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

val KEY_PERSONAGEM: String = "Personagem"

class MainActivity : AppCompatActivity() {
    val listaFotosPersonagens: Array<Int> = arrayOf(
            R.drawable.kenny_mc_cormick,
            R.drawable.eric_cartman,
            R.drawable.kyle_broflovski,
            R.drawable.stan_marsh
    )
    val listaNomesPersonagens: Array<Int> = arrayOf(
            R.string.kenny_mc_cormick,
            R.string.eric_cartman,
            R.string.kyle_broflovski,
            R.string.stan_marsh
    )

    var personagemSelecionado: Int = 0

    val KEY_RESULTADO: String = "BestScore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = this.getSharedPreferences("br.com.conseng.gamecatchsouthpark", android.content.Context.MODE_PRIVATE)

        val ultimoPersonagem: Int = sharedPreferences.getInt(KEY_PERSONAGEM, -1)
        if (ultimoPersonagem < 0) {
            personagemSelecionado = 0
            sharedPreferences.edit().putInt(KEY_PERSONAGEM, personagemSelecionado).apply()
        } else {
            personagemSelecionado = ultimoPersonagem
        }
        mostrarPersonagemCorrente()

        var melhorResultado: Int = sharedPreferences.getInt(KEY_RESULTADO, 0)
        val jogo = intent
        val placarAtual: Int = jogo.getIntExtra(PLACAR_DO_JOGO, -1)
        if (placarAtual > 0) {
            if (placarAtual > melhorResultado) {
                melhorResultado = placarAtual
                sharedPreferences.edit().putInt(KEY_RESULTADO, placarAtual).apply()
                ultimoResultadoTxt.text = String.format(
                        "Novo recorde: %d pontos",
                        placarAtual
                )
            } else {
                ultimoResultadoTxt.text = String.format(
                        "Placar atual: %d pontos",
                        placarAtual
                )
            }
        } else if (melhorResultado > 0) {
            ultimoResultadoTxt.text = String.format(
                    "Melhor resultado: %d pontos",
                    melhorResultado
            )
        }
    }

    fun personagemAnterior(view: View) {
        personagemSelecionado = validarPersonagem(--personagemSelecionado)
        mostrarPersonagemCorrente()
    }

    fun personagemPosterior(view: View) {
        personagemSelecionado = validarPersonagem(++personagemSelecionado)
        mostrarPersonagemCorrente()
    }

    fun iniciarJogo(view: View) {
        val sharedPreferences = this.getSharedPreferences("br.com.conseng.gamecatchsouthpark", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(KEY_PERSONAGEM, personagemSelecionado).apply()

        val jogo = Intent(applicationContext, GameActivity::class.java)
        jogo.putExtra(KEY_PERSONAGEM, getFotoPersonagem(personagemSelecionado))
        startActivity(jogo)
    }

    private fun mostrarPersonagemCorrente() {
        pegaPegaView.setImageResource(getFotoPersonagem(personagemSelecionado))
        personagemTxt.text = getString(getNomePersonagem(personagemSelecionado))
    }

    private fun validarPersonagem(personagem: Int): Int {
        if (personagem < 0) {
            return listaNomesPersonagens.size - 1          // Se rodou para a esquerda, volta ao final da lista
        } else if (personagem >= listaNomesPersonagens.size) {
            return 0                            // Se rodou para a direita, volca para o início da lista
        } else {
            return personagem
        }
    }

    private fun getFotoPersonagem(personagem: Int): Int {
        return listaFotosPersonagens[validarPersonagem(personagem)]
    }

    private fun getNomePersonagem(personagem: Int): Int {
        return listaNomesPersonagens[validarPersonagem(personagem)]
    }
}
