import com.example.eitruck.R

data class MotoristaRanking(
    val posicao: Int,
    val nome: String,
    val pontuacao: Int
) {
    val cor: Int
        get() = when (pontuacao) {
            in 0..250 -> R.color.colorTertiary
            in 251..500 -> R.color.colorSecondary
            in 501..750 -> R.color.colorPrimary
            else -> R.color.colorPrimaryDark
        }
}
