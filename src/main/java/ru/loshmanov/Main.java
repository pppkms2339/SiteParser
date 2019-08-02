package ru.loshmanov;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Задание: Спарсить 100 товаров по этой ссылке​
 * https://flashdeals.aliexpress.com/en.htm? за 1 минуту.​
 * Писать парсер на java не применяя фреймы и с
 * применением ООП. Формат csv, полей чем больше, тем лучше
 *
 */

public class Main {
    //Field name constants to parse
    private static final String PRODUCT_ID_FIELD_NAME = "productId";
    private static final String SELLER_ID_FIELD_NAME = "sellerId";
    private static final String PRODUCT_IMAGE_FIELD_NAME = "productImage";
    private static final String PRODUCT_DETAIL_URL_FIELD_NAME = "productDetailUrl";
    private static final String PRODUCT_TITLE_FIELD_NAME = "productTitle";
    private static final String PRODUCT_MIN_PRICE_FIELD_NAME = "minPrice";
    private static final String PRODUCT_MAX_PRICE_FIELD_NAME = "maxPrice";
    //The maximum number of products to parse
    private static final int PRODUCT_COUNT = 100;
    //List of products after parsing
    private static List<Product> products = new ArrayList<>();

    public static void main(String[] args) {
        Long startTime = System.nanoTime();

        copySiteToDrive();
        fileParse("data/data1.txt");
        fileParse("data/data2.txt");
        fileParse("data/data3.txt");
        printCsv("data/data.csv");

        System.out.println("Algorithm running time: " + (System.nanoTime() - startTime) + " milliseconds");
    }

    /**
     * Prints the product list (List<Product> products)
     * to the csv file
     *
     * @param fileName is a name of the csv file
     */
    private static void printCsv(String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            for (int i = 0; i < products.size(); i++) {
                writer.writeNext(products.get(i).toArray());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a text file and fills the product list
     * (List<Product> products)
     *
     * @param fileName is a name of the txt file (product information file)
     */
    private static void fileParse(String fileName) {
        if (products.size() == PRODUCT_COUNT)
            return;
        int commaIndex, nextCommaIndex;
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                Matcher m = Pattern.compile(PRODUCT_ID_FIELD_NAME).matcher(line);
                while (m.find()) {
                    Product product = new Product();
                    //productId
                    commaIndex = line.indexOf(",", m.end());
                    product.setProductId(line.substring(m.end() + 2, commaIndex));
                    //sellerId
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setSellerId(line.substring(commaIndex + SELLER_ID_FIELD_NAME.length() + 4, nextCommaIndex));
                    //productImage
                    commaIndex = nextCommaIndex;
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setProductImage(line.substring(commaIndex + PRODUCT_IMAGE_FIELD_NAME.length() + 5, nextCommaIndex - 1));
                    //productDetailUrl
                    commaIndex = nextCommaIndex;
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setProductDetailUrl(line.substring(commaIndex + PRODUCT_DETAIL_URL_FIELD_NAME.length() + 5, nextCommaIndex - 1));
                    //productTitle
                    commaIndex = nextCommaIndex;
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setProductTitle(line.substring(commaIndex + PRODUCT_TITLE_FIELD_NAME.length() + 5, nextCommaIndex - 1));
                    //minPrice
                    commaIndex = nextCommaIndex;
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setMinPrice(line.substring(commaIndex + PRODUCT_MIN_PRICE_FIELD_NAME.length() + 5, nextCommaIndex - 1));
                    //maxPrice
                    commaIndex = nextCommaIndex;
                    nextCommaIndex = line.indexOf(",", commaIndex + 1);
                    product.setMaxPrice(line.substring(commaIndex + PRODUCT_MAX_PRICE_FIELD_NAME.length() + 5, nextCommaIndex - 1));

                    products.add(product);
                    if (products.size() == PRODUCT_COUNT)
                        return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies information from the site to text documents (text1.txt,
     * text2.txt, text3.txt)
     *
     */
    private static void copySiteToDrive() {
        String address = "https://gpsfront.aliexpress.com/queryGpsProductAjax.do?callback=jQuery18308576257518279409_1564675515363&widget_id=5547572&platform=pc&limit=50&offset=50&phase=1&productIds2Top=&postback=4e7ab231-6598-443f-9422-ca79761694ca&_=1564675931592";
        writeToFile("data/data1.txt", address);
        address = "https://gpsfront.aliexpress.com/queryGpsProductAjax.do?callback=jQuery18308576257518279409_1564675515363&widget_id=5547572&platform=pc&limit=50&offset=100&phase=1&productIds2Top=&postback=4e7ab231-6598-443f-9422-ca79761694ca&_=1564675931592";
        writeToFile("data/data2.txt", address);
        //Delay to prevent blocking
        try {
            Thread.sleep((long) (9000 * Math.random() + 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        address = "https://gpsfront.aliexpress.com/queryGpsProductAjax.do?callback=jQuery18308576257518279409_1564675515363&widget_id=5547572&platform=pc&limit=50&offset=150&phase=1&productIds2Top=&postback=4e7ab231-6598-443f-9422-ca79761694ca&_=1564675931592";
        writeToFile("data/data3.txt", address);
    }

    /**
     * Used to write data from a site to a file
     *
     * @param fileName is a name of the txt file to write
     * @param address is a site address
     */
    private static void writeToFile(String fileName, String address) {
        int count;
        byte[] buff = new byte[128];
        InputStream is = null;
        OutputStream out = null;

        try {
            is = new URL(address).openStream();
            out = new FileOutputStream(fileName);
            while ((count = is.read(buff)) != -1) {
                out.write(buff, 0, count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
