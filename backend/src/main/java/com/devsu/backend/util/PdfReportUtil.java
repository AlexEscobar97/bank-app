package com.devsu.backend.util;

import com.devsu.backend.entity.Cliente;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.entity.Movimiento;
import com.devsu.backend.repository.MovimientoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PdfReportUtil {

    private PdfReportUtil(){}

    public static byte[] buildPdf(
            Cliente cliente,
            List<Cuenta> cuentas,
            LocalDate inicio,
            LocalDate fin,
            BigDecimal totalCreditos,
            BigDecimal totalDebitos,
            MovimientoRepository movRepo
    ) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Estado de Cuenta", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            doc.add(new Paragraph("Cliente: " + cliente.getNombre() + " (" + cliente.getClienteId() + ")"));
            doc.add(new Paragraph("Rango: " + inicio + " a " + fin));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Cuentas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            PdfPTable tC = new PdfPTable(3);
            tC.addCell("Numero Cuenta");
            tC.addCell("Tipo");
            tC.addCell("Saldo Actual");
            for (Cuenta c : cuentas) {
                tC.addCell(c.getNumeroCuenta());
                tC.addCell(c.getTipoCuenta().name());
                tC.addCell(c.getSaldoActual().toPlainString());
            }
            doc.add(tC);

            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Totales", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            doc.add(new Paragraph("Total Créditos: " + totalCreditos));
            doc.add(new Paragraph("Total Débitos: " + totalDebitos));

            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Movimientos (por cuenta)", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

            for (Cuenta c : cuentas) {
                doc.add(new Paragraph("Cuenta: " + c.getNumeroCuenta()));
                PdfPTable tM = new PdfPTable(4);
                tM.addCell("Fecha");
                tM.addCell("Tipo");
                tM.addCell("Valor");
                tM.addCell("Saldo Disponible");

                List<Movimiento> movs = movRepo.findByCuenta_NumeroCuentaAndFechaBetween(c.getNumeroCuenta(), inicio, fin);
                for (Movimiento m : movs) {
                    tM.addCell(m.getFecha().toString());
                    tM.addCell(m.getTipoMovimiento().name());
                    tM.addCell(m.getValor().toPlainString());
                    tM.addCell(m.getSaldoDisponible().toPlainString());
                }
                doc.add(tM);
                doc.add(Chunk.NEWLINE);
            }

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            return ("PDF Error: " + e.getMessage()).getBytes();
        }
    }
}
