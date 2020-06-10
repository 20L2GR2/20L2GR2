package pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class GeneratePdf {
    public void generatePDF(String path, String data, String firma, String klient, String gdzieFirma, String koszta[][]){
        Document document = new Document();
        try
        {
            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font normal12 = new Font(helvetica, 12);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            Font font = new Font();
            font.setSize(7);
            Paragraph paragraph = new Paragraph("Wystawiono dnia "+data, font);



            document.open();
            document.add(paragraph);

            PdfPTable table = new PdfPTable(2);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));


            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            Paragraph paragraph1 = new Paragraph("Sprzedawca", font1);
            PdfPCell cell = new PdfPCell(paragraph1);
            cell.setBorder(0);
            table.addCell(cell);

            Paragraph paragraph2 = new Paragraph("Nabywca", font1);
            PdfPCell cell2 = new PdfPCell(paragraph2);
            cell2.setBorder(0);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Paragraph(firma, normal12));
            cell3.setBorder(0);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph(klient, normal12));
            cell4.setBorder(0);
            table.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Paragraph(gdzieFirma, normal12));
            cell5.setBorder(0);
            table.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Paragraph(""));
            cell6.setBorder(0);
            table.addCell(cell6);

            table.setSplitRows(true);



            PdfPTable table1 = new PdfPTable(3);
            table1.addCell("Lp");
            table1.addCell(new Paragraph("Usługa", normal12));
            table1.addCell(new Paragraph("Cena usługi",normal12));


            float suma = 0;
            for(int i =0; i<koszta.length; i++){
                table1.addCell(""+(i+1));
                table1.addCell(new Paragraph(koszta[i][0], normal12));
                table1.addCell(koszta[i][1]);
                suma += Float.parseFloat(koszta[i][1]);
            }



            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            Paragraph faktura = new Paragraph("FAKTURA", font1);
            faktura.setAlignment(Element.ALIGN_CENTER);
            document.add(faktura);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Sposób płatności: ................................................................................ (gotówka/przelew)\n" +
                    "Termin płatności: ................................................................................\n" +
                    "Nazwa Banku: MBank\n" +
                    "Numer konta: 1111111111111111 ", normal12));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(table1);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            float czescDziesietna = suma % 1;
            System.out.println(czescDziesietna);
            while(czescDziesietna % 1 != 0)
                czescDziesietna *= 10;
            document.add(new Paragraph("Do zapłaty: "+ (suma)+" złotych\n" +
                    "(słownie "+translation.translacja((long)suma)+" złotych "+translation.translacja((long)czescDziesietna)+" groszy)\n" +
                    "Zapłacono: ......................................................\n" +
                    "Pozostało do zapłaty: .......................................\n" +
                    "Uwagi: .......................................................................................................................................... ", normal12));


            document.close();
            writer.close();
        } catch (
        DocumentException e)
        {
            e.printStackTrace();
        } catch (
        FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
