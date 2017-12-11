package br.com.conseng.gamecatchsouthpark

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*

val PLACAR_DO_JOGO: String = "placar_do_jogo"

class GameActivity : AppCompatActivity() {

    var placarAtual: Int = 0
    var jogando: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val corrente = intent
        var personagemSelecionado: Int = corrente.getIntExtra(
                KEY_PERSONAGEM,
                R.drawable.kenny_mc_cormick
        )
        pegaPegaView.setImageResource(personagemSelecionado)

        jogando = true
        object : CountDownTimer(10000, 500) {
            override fun onFinish() {
                jogando = false
                tempoJogo.text = "Jogo terminou"
            }

            override fun onTick(millisUntilFinished: Long) {
                moveImagem()
                tempoJogo.text = String.format("Faltam %d seconds", millisUntilFinished / 1000)
            }
        }.start()
    }

    fun sairJogo(view: View) {
        val placar = Intent(applicationContext, MainActivity::class.java)
        placar.putExtra(PLACAR_DO_JOGO, placarAtual)
        startActivity(placar)
    }

    fun maisUmPonto(view: View) {
        if (jogando) {
            placarAtual++
            placarDoJogo.text = String.format("Placar atual: %d acertos", placarAtual)
        }
    }

    private fun moveImagem() {
        //TODO: mover a imagem de forma aleatória
    }
}
