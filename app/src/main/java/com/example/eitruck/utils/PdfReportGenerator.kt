package com.example.eitruck.utils

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import com.example.eitruck.R
import java.io.File
import java.io.FileOutputStream

object PdfReportGenerator {

    fun gerarRelatorioTratativa(
        context: Context,
        titulo: String,
        corpo: String,
        motorista: String?,
        placa: String?,
        dataInicio: String?,
        dataFim: String?,
        totalInfracoes: String?,
        leves: String?,
        medias: String?,
        graves: String?,
        gravissimas: String?,

        ) {
        // Verificações iniciais para evitar crashes
        if (motorista.isNullOrEmpty() || placa.isNullOrEmpty()) {
            Toast.makeText(context, "Erro: Dados insuficientes para gerar relatório (motorista ou placa ausentes).", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 (595x842 pontos)
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            val corPrincipal = Color.parseColor("#1C3A62")
            val corFundoInfo = Color.parseColor("#F0F0F0")
            val margemLateral = 80f
            var y = 80f

            // ------------------ TÍTULO (Cabeçalho) ------------------
            paint.textSize = 24f
            paint.isFakeBoldText = true
            paint.color = corPrincipal
            canvas.drawText("RELATÓRIO DE VIAGEM", margemLateral, y, paint)
            y += 5f

            paint.color = corPrincipal
            canvas.drawRect(margemLateral, y, pageInfo.pageWidth - margemLateral, y + 3f, paint)
            y += 35f

            // ------------------ BLOCO DE INFORMAÇÕES GERAIS ------------------
            paint.color = Color.BLACK
            paint.textSize = 14f
            paint.isFakeBoldText = false

            val infoList = listOfNotNull(
                motorista?.let { "Motorista: $it" },
                placa?.let { "Placa: $it" },
                dataInicio?.let { "Início: $it" },
                dataFim?.let { "Fim: $it" },
                totalInfracoes?.let { "Total de Infrações: $it" },
                leves?.let { "Leves: $it" },
                medias?.let { "Médias: $it" },
                graves?.let { "Graves: $it" },
                gravissimas?.let { "Gravíssimas: $it" }
            )

            val espacamentoLinha = 25f
            val paddingVertical = 25f
            val paddingHorizontal = 20f
            val alturaTexto = infoList.size * espacamentoLinha

            paint.color = corFundoInfo
            canvas.drawRect(
                margemLateral - paddingHorizontal,
                y - paddingVertical,
                pageInfo.pageWidth - (margemLateral - paddingHorizontal),
                y + alturaTexto + paddingVertical,
                paint
            )

            paint.color = corPrincipal
            var textoY = y
            for (info in infoList) {
                canvas.drawText(info, margemLateral, textoY, paint)
                textoY += espacamentoLinha
            }

            y += alturaTexto + (paddingVertical * 2)

            // ------------------ TÍTULO DA SEÇÃO DE TRATATIVA ------------------
            paint.textSize = 18f
            paint.isFakeBoldText = true
            paint.color = corPrincipal
            y += 20f // espaçamento extra entre o bloco e o título
            canvas.drawText("DESCRIÇÃO DA TRATATIVA", margemLateral, y, paint)
            y += 30f

            // ------------------ CORPO DO TEXTO (Com quebra de linha) ------------------
            paint.textSize = 16f
            paint.isFakeBoldText = false
            paint.color = Color.DKGRAY
            canvas.drawText(titulo, margemLateral, y, paint)
            y += 25f

            val textPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 14f
                isFakeBoldText = false
            }

            val text = corpo
            val textWidth = pageInfo.pageWidth - 2 * margemLateral

            val staticLayout = StaticLayout.Builder
                .obtain(text, 0, text.length, TextPaint(textPaint), textWidth.toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

            canvas.save()
            canvas.translate(margemLateral, y)
            staticLayout.draw(canvas)
            canvas.restore()

            // ------------------ RODAPÉ ------------------
            // Linha separadora do rodapé
            val linhaRodapeY = pageInfo.pageHeight - 120f
            paint.color = Color.GRAY
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawLine(margemLateral, linhaRodapeY, pageInfo.pageWidth - margemLateral, linhaRodapeY, paint)
            paint.style = Paint.Style.FILL

            y += 50f
            val logoDrawable = context.resources.getDrawable(R.drawable.logo_eitruck, null)

            if (logoDrawable is BitmapDrawable) {
                val originalBitmap = logoDrawable.bitmap

                val logoWidth = 90
                val logoHeight = 30
                val logoX = (pageInfo.pageWidth / 2) - (logoWidth / 2)
                val logoY = pageInfo.pageHeight - logoHeight - 80f

                // Desenho do Logo
                canvas.drawBitmap(
                    Bitmap.createScaledBitmap(originalBitmap, logoWidth, logoHeight, true),
                    logoX.toFloat(),
                    logoY,
                    paint
                )
            }

            paint.textSize = 12f
            paint.color = Color.GRAY
            paint.textAlign = Paint.Align.CENTER
            val textY = pageInfo.pageHeight - 40f

            canvas.drawText(
                "Relatório gerado pelo aplicativo EiTruck",
                (pageInfo.pageWidth / 2).toFloat(),
                textY,
                paint
            )
            pdfDocument.finishPage(page)

            // --- Salva PDF e Abre ---
            val fileName = "Relatorio_Tratativa_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            Toast.makeText(context, "Relatório salvo!", Toast.LENGTH_LONG).show()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("PdfReportGenerator", "Erro ao gerar PDF: ${e.message}", e)
            Toast.makeText(context, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}