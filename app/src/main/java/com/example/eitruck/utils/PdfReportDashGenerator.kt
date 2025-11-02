import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.core.content.FileProvider
import com.example.eitruck.R
import java.io.File
import java.io.FileOutputStream

object PdfReportDashGenerator {

    fun gerarRelatorioDash(context: Context, titulo: String, container: LinearLayout) {
        val paint = Paint()
        val pdfDocument = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margemLateral = 40f

        // Converter container em Bitmap
        val bitmap = container.drawToBitmap()

        // Cria a página
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Título
        paint.textSize = 20f
        paint.isFakeBoldText = true
        paint.color = Color.GRAY
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(titulo, pageWidth / 2f, 50f, paint)

        // Área disponível para o bitmap
        val availableWidth = pageWidth - 2 * margemLateral
        val availableHeight = pageHeight - 150 - 70

        // Mantém proporção
        val scale = minOf(availableWidth / bitmap.width.toFloat(), availableHeight / bitmap.height.toFloat())
        val destWidth = (bitmap.width * scale).toInt()
        val destHeight = (bitmap.height * scale).toInt()
        val destLeft = (pageWidth - destWidth) / 2
        val destTop = 70

        val destRect = Rect(destLeft, destTop, destLeft + destWidth, destTop + destHeight)
        canvas.drawBitmap(bitmap, null, destRect, paint)

        // Rodapé
        val linhaRodapeY = pageHeight - 120f
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawLine(margemLateral, linhaRodapeY, pageWidth - margemLateral, linhaRodapeY, paint)
        paint.style = Paint.Style.FILL

        // Logo
        val logoDrawable = context.resources.getDrawable(R.drawable.logo_eitruck, null)
        if (logoDrawable is BitmapDrawable) {
            val originalBitmap = logoDrawable.bitmap
            val logoWidth = 90
            val logoHeight = 30
            val logoX = (pageWidth / 2) - (logoWidth / 2)
            val logoY = pageHeight - logoHeight - 80f
            canvas.drawBitmap(Bitmap.createScaledBitmap(originalBitmap, logoWidth, logoHeight, true), logoX.toFloat(), logoY, paint)
        }

        // Texto rodapé
        paint.textSize = 12f
        paint.color = Color.GRAY
        paint.textAlign = Paint.Align.CENTER
        val textY = pageHeight - 40f
        canvas.drawText("Relatório gerado pelo aplicativo EiTruck", pageWidth / 2f, textY, paint)

        pdfDocument.finishPage(page)

        // --- Salva PDF e Abre ---
        val fileName = "Relatorio_Dash_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(null), fileName)

        FileOutputStream(file).use {
            pdfDocument.writeTo(it)
        }
        pdfDocument.close()

        Toast.makeText(context, "Relatório salvo!", Toast.LENGTH_LONG).show()

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}
