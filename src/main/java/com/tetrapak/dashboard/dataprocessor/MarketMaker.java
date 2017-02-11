/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.dashboard.dataprocessor;

import com.tetrapak.dashboard.models.MarketBean;
import java.util.HashMap;
import java.util.Map;

/**
 * This class builds the markets. The data and source is contained in file
 * 'geographical master.xlsx'.
 *
 * @author SEPALMM
 */
public class MarketMaker {

    private static Map<String, MarketBean> marketMap;

    private static void makeMarkets() {
// Initiate marketMap       
        marketMap = new HashMap<>();

//  Populate map with market data
marketMap.put("AE",  new MarketBean("AE", "UNITED ARAB EMIRATES", "077", "UNITED ARAB EMIRATES", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("AF",  new MarketBean("AF", "AFGHANISTAN", "092", "AFGHANISTAN", "G094", "PAKISTAN", "GME&A"));
marketMap.put("AL",  new MarketBean("AL", "ALBANIA", "034", "ALBANIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("AM",  new MarketBean("AM", "ARMENIA", "061", "ARMENIA", "G032", "TURKEY AREA", "GME&A"));
marketMap.put("AN",  new MarketBean("AN", "NETHERLANDS ANTILLES", "132", "PANAMA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("AO",  new MarketBean("AO", "ANGOLA", "270", "ANGOLA", "E021", "IBERIA", "E&CA"));
marketMap.put("AR",  new MarketBean("AR", "ARGENTINA", "157", "ARGENTINA", "G157", "SOUTHERN CONE", "NC&SA"));
marketMap.put("AT",  new MarketBean("AT", "AUSTRIA", "026", "AUSTRIA", "E009", "MID EUROPE", "E&CA"));
marketMap.put("AU",  new MarketBean("AU", "AUSTRALIA", "170", "AUSTRALIA", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("AZ",  new MarketBean("AZ", "AZERBAIJAN", "060", "AZERBAIJAN", "G032", "TURKEY AREA", "GME&A"));
marketMap.put("BA",  new MarketBean("BA", "BOSNIA AND HERZEGOVINA", "057", "BOSNIA AND HERZEGOVINA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("BB",  new MarketBean("BB", "BARBADOS", "145", "BARBADOS", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("BD",  new MarketBean("BD", "BANGLADESH", "071", "BANGLADESH", "G095", "SOUTH ASIAN MARKETS", "SAEA&O"));
marketMap.put("BE",  new MarketBean("BE", "BELGIUM", "015", "BELGIUM", "E017", "NORTH WEST EUROPE", "E&CA"));
marketMap.put("BG",  new MarketBean("BG", "BULGARIA", "031", "BULGARIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("BH",  new MarketBean("BH", "BAHRAIN", "075", "BAHRAIN", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("BI",  new MarketBean("BI", "BURUNDI", "267", "BURUNDI", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("BO",  new MarketBean("BO", "BOLIVIA", "159", "BOLIVIA", "E162", "ANDINA", "NC&SA"));
marketMap.put("BR",  new MarketBean("BR", "BRAZIL", "154", "BRAZIL", "G154", "BRAZIL", "NC&SA"));
marketMap.put("BT",  new MarketBean("BT", "BHUTAN", "096", "BHUTAN", "G095", "SOUTH ASIAN MARKETS", "SAEA&O"));
marketMap.put("BW",  new MarketBean("BW", "BOTSWANA", "278", "BOTSWANA", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("BY",  new MarketBean("BY", "BELARUS", "052", "BELARUS", "E054", "RUBECA", "E&CA"));
marketMap.put("BZ",  new MarketBean("BZ", "BELIZE", "126", "BELIZE", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("CA",  new MarketBean("CA", "CANADA", "120", "CANADA", "G121", "USA & CANADA", "NC&SA"));
marketMap.put("CH",  new MarketBean("CH", "SWITZERLAND", "025", "SWITZERLAND", "E009", "MID EUROPE", "E&CA"));
marketMap.put("CL",  new MarketBean("CL", "CHILE", "158", "CHILE", "G157", "SOUTHERN CONE", "NC&SA"));
marketMap.put("CM",  new MarketBean("CM", "CAMEROON", "259", "CAMEROON", "G257", "WEST AFRICA", "GME&A"));
marketMap.put("CN",  new MarketBean("CN", "CHINA", "106", "CHINA", "G106", "GREATER CHINA", "GC"));
marketMap.put("CO",  new MarketBean("CO", "COLOMBIA", "162", "COLOMBIA", "E162", "ANDINA", "NC&SA"));
marketMap.put("CR",  new MarketBean("CR", "COSTA RICA", "131", "COSTA RICA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("CU",  new MarketBean("CU", "CUBA", "132", "PANAMA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("CY",  new MarketBean("CY", "CYPRUS", "080", "CYPRUS", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("CZ",  new MarketBean("CZ", "CZECH REPUBLIC", "047", "CZECH REPUBLIC", "E026", "POLAND & DANUBE", "E&CA"));
marketMap.put("DE",  new MarketBean("DE", "GERMANY", "009", "GERMANY", "E009", "MID EUROPE", "E&CA"));
marketMap.put("DJ",  new MarketBean("DJ", "DJIBOUTI", "296", "DJIBOUTI", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("DK",  new MarketBean("DK", "DENMARK", "003", "DENMARK", "E001", "NORDICS", "E&CA"));
marketMap.put("DO",  new MarketBean("DO", "DOMINICAN REPUBLIC", "136", "DOMINICAN REPUBLIC", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("DZ",  new MarketBean("DZ", "ALGERIA", "235", "ALGERIA", "G233", "MAGHREB", "GME&A"));
marketMap.put("EC",  new MarketBean("EC", "ECUADOR", "161", "ECUADOR", "E162", "ANDINA", "NC&SA"));
marketMap.put("EE",  new MarketBean("EE", "ESTONIA", "040", "ESTONIA", "E001", "NORDICS", "E&CA"));
marketMap.put("EG",  new MarketBean("EG", "EGYPT", "231", "EGYPT", "G231", "EGYPT", "GME&A"));
marketMap.put("ES",  new MarketBean("ES", "SPAIN", "021", "SPAIN", "E021", "IBERIA", "E&CA"));
marketMap.put("FI",  new MarketBean("FI", "FINLAND", "006", "FINLAND", "E001", "NORDICS", "E&CA"));
marketMap.put("FJ",  new MarketBean("FJ", "FIJI", "179", "FIJI", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("FO",  new MarketBean("FO", "FAROE ISLANDS", "003", "DENMARK", "E001", "NORDICS", "E&CA"));
marketMap.put("FR",  new MarketBean("FR", "FRANCE", "020", "FRANCE", "G020", "FRANCE", "E&CA"));
marketMap.put("GB",  new MarketBean("GB", "UNITED KINGDOM", "017", "UNITED KINGDOM", "E017", "NORTH WEST EUROPE", "E&CA"));
marketMap.put("GE",  new MarketBean("GE", "GEORGIA", "062", "GEORGIA", "G032", "TURKEY AREA", "GME&A"));
marketMap.put("GH",  new MarketBean("GH", "GHANA", "255", "GHANA", "G257", "WEST AFRICA", "GME&A"));
marketMap.put("GP",  new MarketBean("GP", "GUADELOUPE", "020", "FRANCE", "G020", "FRANCE", "E&CA"));
marketMap.put("GR",  new MarketBean("GR", "GREECE", "033", "GREECE", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("GT",  new MarketBean("GT", "GUATEMALA", "127", "GUATEMALA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("GY",  new MarketBean("GY", "GUYANA", "151", "GUYANA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("HK",  new MarketBean("HK", "HONG KONG", "108", "HONG KONG", "G106", "GREATER CHINA", "GC"));
marketMap.put("HN",  new MarketBean("HN", "HONDURAS", "129", "HONDURAS", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("HR",  new MarketBean("HR", "CROATIA", "044", "CROATIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("HT",  new MarketBean("HT", "HAITI", "135", "HAITI", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("HU",  new MarketBean("HU", "HUNGARY", "028", "HUNGARY", "E026", "POLAND & DANUBE", "E&CA"));
marketMap.put("ID",  new MarketBean("ID", "INDONESIA", "103", "INDONESIA", "G103", "INDONESIA", "SAEA&O"));
marketMap.put("IE",  new MarketBean("IE", "IRELAND", "019", "IRELAND", "E017", "NORTH WEST EUROPE", "E&CA"));
marketMap.put("IL",  new MarketBean("IL", "ISRAEL", "083", "ISRAEL", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("IN",  new MarketBean("IN", "INDIA", "095", "INDIA", "G095", "SOUTH ASIAN MARKETS", "SAEA&O"));
marketMap.put("IQ",  new MarketBean("IQ", "IRAQ", "090", "IRAQ", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("IR",  new MarketBean("IR", "IRAN", "091", "IRAN", "G091", "IRAN", "GME&A"));
marketMap.put("IS",  new MarketBean("IS", "ICELAND", "005", "ICELAND", "E001", "NORDICS", "E&CA"));
marketMap.put("IT",  new MarketBean("IT", "ITALY", "024", "ITALY", "G024", "ITALY", "E&CA"));
marketMap.put("JM",  new MarketBean("JM", "JAMAICA", "137", "JAMAICA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("JO",  new MarketBean("JO", "JORDAN", "084", "JORDAN", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("JP",  new MarketBean("JP", "JAPAN", "109", "JAPAN", "E109", "JAPAN & KOREA", "SAEA&O"));
marketMap.put("KE",  new MarketBean("KE", "KENYA", "289", "KENYA", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("KG",  new MarketBean("KG", "KYRGYZSTAN", "063", "KYRGYZSTAN", "E054", "RUBECA", "E&CA"));
marketMap.put("KH",  new MarketBean("KH", "CAMBODIA", "117", "CAMBODIA", "E100", "THAILAND", "SAEA&O"));
marketMap.put("KR",  new MarketBean("KR", "KOREA, REPUBLIC OF", "110", "KOREA, REPUBLIC OF", "E109", "JAPAN & KOREA", "SAEA&O"));
marketMap.put("KW",  new MarketBean("KW", "KUWAIT", "088", "KUWAIT", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("KZ",  new MarketBean("KZ", "KAZAKHSTAN", "055", "KAZAKHSTAN", "E054", "RUBECA", "E&CA"));
marketMap.put("LB",  new MarketBean("LB", "LEBANON", "082", "LEBANON", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("LI",  new MarketBean("LI", "LIECHTENSTEIN", "016", "LIECHTENSTEIN", "E009", "MID EUROPE", "E&CA"));
marketMap.put("LK",  new MarketBean("LK", "SRI LANKA", "098", "SRI LANKA", "G095", "SOUTH ASIAN MARKETS", "SAEA&O"));
marketMap.put("LT",  new MarketBean("LT", "LITHUANIA", "042", "LITHUANIA", "E001", "NORDICS", "E&CA"));
marketMap.put("LU",  new MarketBean("LU", "LUXEMBOURG", "039", "LUXEMBOURG", "E017", "NORTH WEST EUROPE", "E&CA"));
marketMap.put("LV",  new MarketBean("LV", "LATVIA", "041", "LATVIA", "E001", "NORDICS", "E&CA"));
marketMap.put("LY",  new MarketBean("LY", "LIBYA", "233", "LIBYA", "G231", "EGYPT", "GME&A"));
marketMap.put("MA",  new MarketBean("MA", "MOROCCO", "236", "MOROCCO", "G233", "MAGHREB", "GME&A"));
marketMap.put("MD",  new MarketBean("MD", "MOLDOVA", "053", "MOLDOVA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("ME",  new MarketBean("ME", "MONTENEGRO", "045", "SERBIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("MG",  new MarketBean("MG", "MADAGASCAR", "285", "MADAGASCAR", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("MK",  new MarketBean("MK", "MACEDONIA", "046", "MACEDONIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("MM",  new MarketBean("MM", "MYANMAR", "099", "MYANMAR", "E100", "THAILAND", "SAEA&O"));
marketMap.put("MN",  new MarketBean("MN", "MONGOLIA", "118", "MONGOLIA", "G106", "GREATER CHINA", "GC"));
marketMap.put("MQ",  new MarketBean("MQ", "MARTINIQUE", "164", "MARTINIQUE", "G020", "FRANCE", "E&CA"));
marketMap.put("MR",  new MarketBean("MR", "MAURITANIA", "242", "MAURITANIA", "G233", "MAGHREB", "GME&A"));
marketMap.put("MT",  new MarketBean("MT", "MALTA", "035", "MALTA", "G024", "ITALY", "E&CA"));
marketMap.put("MU",  new MarketBean("MU", "MAURITIUS", "272", "SOUTH AFRICA", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("MW",  new MarketBean("MW", "MALAWI", "282", "MALAWI", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("MX",  new MarketBean("MX", "MEXICO", "125", "MEXICO", "G125", "MEXICO", "NC&SA"));
marketMap.put("MY",  new MarketBean("MY", "MALAYSIA", "101", "MALAYSIA", "G101", "MALAYSIA & PHILIPPINES", "SAEA&O"));
marketMap.put("MZ",  new MarketBean("MZ", "MOZAMBIQUE", "284", "MOZAMBIQUE", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("NA",  new MarketBean("NA", "NAMIBIA", "273", "NAMIBIA", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("NC",  new MarketBean("NC", "NEW CALEDONIA", "185", "NEW CALEDONIA", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("NE",  new MarketBean("NE", "NIGER", "253", "NIGER", "G257", "WEST AFRICA", "GME&A"));
marketMap.put("NG",  new MarketBean("NG", "NIGERIA", "257", "NIGERIA", "G257", "WEST AFRICA", "GME&A"));
marketMap.put("NI",  new MarketBean("NI", "NICARAGUA", "130", "NICARAGUA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("NL",  new MarketBean("NL", "NETHERLANDS", "014", "NETHERLANDS", "E017", "NORTH WEST EUROPE", "E&CA"));
marketMap.put("NO",  new MarketBean("NO", "NORWAY", "002", "NORWAY", "E001", "NORDICS", "E&CA"));
marketMap.put("NP",  new MarketBean("NP", "NEPAL", "093", "NEPAL", "G095", "SOUTH ASIAN MARKETS", "SAEA&O"));
marketMap.put("NZ",  new MarketBean("NZ", "NEW ZEALAND", "171", "NEW ZEALAND", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("OM",  new MarketBean("OM", "OMAN", "078", "OMAN", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("PA",  new MarketBean("PA", "PANAMA", "132", "PANAMA", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("PE",  new MarketBean("PE", "PERU", "160", "PERU", "E162", "ANDINA", "NC&SA"));
marketMap.put("PF",  new MarketBean("PF", "FRENCH POLYNESIA", "173", "FRENCH POLYNESIA", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("PG",  new MarketBean("PG", "PAPUA NEW GUINEA", "175", "PAPUA NEW GUINEA", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("PH",  new MarketBean("PH", "PHILIPPINES", "105", "PHILIPPINES", "G101", "MALAYSIA & PHILIPPINES", "SAEA&O"));
marketMap.put("PK",  new MarketBean("PK", "PAKISTAN", "094", "PAKISTAN", "G094", "PAKISTAN", "GME&A"));
marketMap.put("PL",  new MarketBean("PL", "POLAND", "011", "POLAND", "E026", "POLAND & DANUBE", "E&CA"));
marketMap.put("PS",  new MarketBean("PS", "PALESTINE", "302", "PALESTINE", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("PT",  new MarketBean("PT", "PORTUGAL", "022", "PORTUGAL", "E021", "IBERIA", "E&CA"));
marketMap.put("PY",  new MarketBean("PY", "PARAGUAY", "155", "PARAGUAY", "G154", "BRAZIL", "NC&SA"));
marketMap.put("QA",  new MarketBean("QA", "QATAR", "076", "QATAR", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("RE",  new MarketBean("RE", "RÉUNION", "286", "RÉUNION", "G020", "FRANCE", "E&CA"));
marketMap.put("RO",  new MarketBean("RO", "ROMANIA", "030", "ROMANIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("RS",  new MarketBean("RS", "SERBIA", "045", "SERBIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("RU",  new MarketBean("RU", "RUSSIAN FEDERATION", "054", "RUSSIAN FEDERATION", "E054", "RUBECA", "E&CA"));
marketMap.put("RW",  new MarketBean("RW", "RWANDA", "265", "RWANDA", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("SA",  new MarketBean("SA", "SAUDI ARABIA", "085", "SAUDI ARABIA", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("SB",  new MarketBean("SB", "SOLOMON ISLANDS", "181", "SOLOMON ISLANDS", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("SC",  new MarketBean("SC", "SEYCHELLES", "289", "KENYA", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("SD",  new MarketBean("SD", "SUDAN", "232", "SUDAN", "G231", "EGYPT", "GME&A"));
marketMap.put("SE",  new MarketBean("SE", "SWEDEN", "001", "SWEDEN", "E001", "NORDICS", "E&CA"));
marketMap.put("SG",  new MarketBean("SG", "SINGAPORE", "112", "SINGAPORE", "G101", "MALAYSIA & PHILIPPINES", "SAEA&O"));
marketMap.put("SI",  new MarketBean("SI", "SLOVENIA", "043", "SLOVENIA", "E033", "SOUTH EASTERN EUROPE", "E&CA"));
marketMap.put("SK",  new MarketBean("SK", "SLOVAKIA", "048", "SLOVAKIA", "E026", "POLAND & DANUBE", "E&CA"));
marketMap.put("SN",  new MarketBean("SN", "SENEGAL", "243", "SENEGAL", "G257", "WEST AFRICA", "GME&A"));
marketMap.put("SR",  new MarketBean("SR", "SURINAME", "152", "SURINAME", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("SV",  new MarketBean("SV", "EL SALVADOR", "128", "EL SALVADOR", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("SY",  new MarketBean("SY", "SYRIA", "081", "SYRIA", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("TH",  new MarketBean("TH", "THAILAND", "100", "THAILAND", "E100", "THAILAND", "SAEA&O"));
marketMap.put("TJ",  new MarketBean("TJ", "TAJIKISTAN", "059", "TAJIKISTAN", "E054", "RUBECA", "E&CA"));
marketMap.put("TM",  new MarketBean("TM", "TURKMENISTAN", "058", "TURKMENISTAN", "E054", "RUBECA", "E&CA"));
marketMap.put("TN",  new MarketBean("TN", "TUNISIA", "234", "TUNISIA", "G233", "MAGHREB", "GME&A"));
marketMap.put("TR",  new MarketBean("TR", "TURKEY", "032", "TURKEY", "G032", "TURKEY AREA", "GME&A"));
marketMap.put("TT",  new MarketBean("TT", "TRINIDAD AND TOBAGO", "138", "TRINIDAD AND TOBAGO", "G132", "CENTRAL A. & CARIBBEAN", "NC&SA"));
marketMap.put("TW",  new MarketBean("TW", "TAIWAN", "107", "TAIWAN", "G106", "GREATER CHINA", "GC"));
marketMap.put("TZ",  new MarketBean("TZ", "TANZANIA", "288", "TANZANIA", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("UA",  new MarketBean("UA", "UKRAINE", "051", "UKRAINE", "E054", "RUBECA", "E&CA"));
marketMap.put("UG",  new MarketBean("UG", "UGANDA", "291", "UGANDA", "G289", "EAST AFRICA", "GME&A"));
marketMap.put("US",  new MarketBean("US", "UNITED STATES", "121", "UNITED STATES", "G121", "USA & CANADA", "NC&SA"));
marketMap.put("UY",  new MarketBean("UY", "URUGUAY", "156", "URUGUAY", "G157", "SOUTHERN CONE", "NC&SA"));
marketMap.put("UZ",  new MarketBean("UZ", "UZBEKISTAN", "056", "UZBEKISTAN", "E054", "RUBECA", "E&CA"));
marketMap.put("VE",  new MarketBean("VE", "VENEZUELA", "150", "VENEZUELA", "E162", "ANDINA", "NC&SA"));
marketMap.put("VN",  new MarketBean("VN", "VIET NAM", "115", "VIET NAM", "G115", "VIETNAM", "SAEA&O"));
marketMap.put("WS",  new MarketBean("WS", "SAMOA", "178", "SAMOA", "G170", "OCEANIA", "SAEA&O"));
marketMap.put("XI",  new MarketBean("XI", "PALESTINE", "302", "PALESTINE", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("YE",  new MarketBean("YE", "YEMEN", "079", "YEMEN", "G085", "ARABIA AREA", "GME&A"));
marketMap.put("ZA",  new MarketBean("ZA", "SOUTH AFRICA", "272", "SOUTH AFRICA", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("ZM",  new MarketBean("ZM", "ZAMBIA", "281", "ZAMBIA", "G272", "SOUTH AFRICA", "GME&A"));
marketMap.put("ZW",  new MarketBean("ZW", "ZIMBABWE", "279", "ZIMBABWE", "G272", "SOUTH AFRICA", "GME&A"));
    }

    public static Map<String, MarketBean> getMarketMap() {
        makeMarkets();
        return marketMap;
    }

}
