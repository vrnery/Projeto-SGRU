/*
Instruções PDF
--Nova Página
document.newPage();
--Background
pageSize.setBackgroundColor(new java.awt.Color(0xFF, 0xFF, 0xDE));
--Imagem
Image figura = Image.getInstance("C:\\imagem.jpg");
document.add(figura);
--Fonte (Lembre-se de colocar o objeto da fonte na tag do parágrafo)
Font f = new Font(FontFamily.COURIER, 20, Font.BOLD);
Paragraph p1 = new Paragraph("Meu primeiro arquivo PDF!", f);
--Alinhamento
p1.setAlignment(Element.ALIGN_CENTER);
--Espaçamento entre linhas
p1.setSpacingAfter(20);
--Gerar Tabelas
PdfPTable table = new PdfPTable("Número de Colunas");
PdfPTable table = new PdfPTable(3);
//Aqui o cabeçalho da tabela
PdfPCell header = new PdfPCell(new Paragraph("Algumas Palavaras Reservadas do Java"));
//Mesclando a linha do cabeçalho para 3 colunas
header.setColspan(3);
table.addCell(header); 
table.addCell("abstract");
table.addCell("extends");
table.addCell("import");
table.addCell("while");
table.addCell("if");
table.addCell("switch");
doc.add(table);
--Definir Tamanho para as colunas
PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.2f, 0.6f });
--Definir Tamanho da tabela
table.setWidthPercentage(60.0f);
--Definir alinhamento da tabela
table.setHorizontalAlignment(Element.ALIGN_RIGHT);
--Mudar cores da tabela 
header.setBackgroundColor(BaseColor.YELLOW);
header.setBorderWidthBottom(2.0f);
header.setBorderColorBottom(BaseColor.BLUE);
header.setBorder(Rectangle.BOTTOM);
 */

/*
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author 10070187
 
public class ExemploPDF {

    
    public static void main(String[] args) {
         // criação do documento
          Document document = new Document();
          try {
             
              PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\10070187\\Documents\\\\PDF_DevMedia.pdf"));
              document.open();
             
              // adicionando um parágrafo no documento
              document.add(new Paragraph("Gerando PDF - Java"));
}
          catch(DocumentException | IOException de) {
              System.err.println(de.getMessage());
          }
          document.close();
    }
    
}*/
