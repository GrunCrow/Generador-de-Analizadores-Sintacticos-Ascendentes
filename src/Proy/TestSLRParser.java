package proy;

import generated.*;
import parserjj.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

*/


public class TestSLRParser {

    public static void main(String[] args) {
        try {
            // Obtener los valores de los símbolos terminales y no terminales
            Map<Integer, String> terminalSymbols = getSymbolConstants(TokenConstants.class);
            Map<Integer, String> nonTerminalSymbols = getSymbolConstants(SymbolConstants.class);

            // Crear una instancia del analizador SLR personalizado
            CustomSLRParser parser = new CustomSLRParser();

            // Crear el libro de Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("SLR Analysis Table");

            // Crear la primera fila con los encabezados de columna
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Estado");
            headerRow.createCell(1).setCellValue("<EOF>");

            // Agregar los símbolos terminales a la primera fila
            int column = 2;
            for (String symbol : terminalSymbols.values()) {
                headerRow.createCell(column).setCellValue(symbol);
                column++;
            }

            // Agregar los símbolos no terminales a la primera fila
            for (String symbol : nonTerminalSymbols.values()) {
                headerRow.createCell(column).setCellValue(symbol);
                column++;
            }

            // Generar las filas restantes con los datos de la tabla
            for (int state = 0; state < parser.getActionTable().length; state++) {
                Row row = sheet.createRow(state + 1);
                row.createCell(0).setCellValue(state);

                for (int symbol = 0; symbol < parser.getActionTable()[state].length; symbol++) {
                    ActionElement action = parser.getActionTable()[state][symbol];
                    Cell cell = row.createCell(symbol + 1);

                    if (action != null) {
                        int actionType = action.getType();
                        int value = action.getValue();

                        if (actionType == ActionElement.SHIFT) {
                            cell.setCellValue("s" + value);
                        } else if (actionType == ActionElement.REDUCE) {
                            cell.setCellValue("r" + value);
                        } else if (actionType == ActionElement.ACCEPT) {
                            cell.setCellValue("acc");
                        }
                    }
                }
            }

            // Ajustar el tamaño de las columnas para que se ajusten al contenido
            for (int i = 0; i <= column; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar el libro de Excel
            String outputFilePath = "SLRAnalysisTable.xlsx";
            workbook.write(new FileOutputStream(outputFilePath));
            workbook.close();

            System.out.println("Tabla de análisis SLR generada en el archivo: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, String> getSymbolConstants(Class<?> symbolConstantsClass) {
        Map<Integer, String> symbolConstants = new HashMap<>();
        try {
            Field[] fields = symbolConstantsClass.getFields();
            for (Field field : fields) {
                if (field.getType() == int.class && Modifier.isStatic(field.getModifiers())) {
                    int value = field.getInt(null);
                    String name = field.getName();
                    symbolConstants.put(value, name);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return symbolConstants;
    }

    // Clase de analizador SLR personalizado que extiende SLRParser
    private static class CustomSLRParser extends SLRParser {
        // Aquí se define la implementación específica del analizador SLR
        // Se deben proporcionar las tablas de acciones y desplazamientos (actionTable y gotoTable)
        // y las reglas de reducción (rule)
    }
}
