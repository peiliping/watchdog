package com.github.ztemp;


import com.github.watchdog.common.Util;
import com.huobi.client.GenericClient;
import com.huobi.client.MarketClient;
import com.huobi.client.req.market.SubMarketTradeRequest;
import com.huobi.constant.HuobiOptions;
import com.huobi.model.generic.Symbol;
import com.huobi.model.market.MarketTrade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Slf4j(topic = "watchdog-data")
public class Start {


    public static void main(String[] args) {

        String rest = "https://www.huobi.be/-/x/pro";
        String websocket = "wss://api.huobiasia.vip";
        String symbolStr = args[0];
        Long volumeFilter = Long.parseLong(args[1]);

        HuobiOptions options = HuobiOptions.builder().restHost(rest).websocketHost(websocket).build();

        GenericClient genericClient = GenericClient.create(options);
        MarketClient marketClient = MarketClient.create(options);

        List<Symbol> symbols = genericClient.getSymbols();
        Symbol symbol = symbols.stream().filter(st -> symbolStr.equals(st.getSymbol())).findFirst().get();
        Validate.notNull(symbol);

        marketClient.subMarketTrade(SubMarketTradeRequest.builder().symbol(symbol.getSymbol()).build(), marketTradeEvent -> {
            List<MarketTrade> list = marketTradeEvent.getList();
            double totalAmount = 0d, totalVolume = 0d;
            double priceLow = Double.MAX_VALUE, priceHigh = Double.MIN_VALUE;
            MarketTrade first = list.get(0);
            for (MarketTrade marketTrade : list) {
                double price = marketTrade.getPrice().doubleValue();
                double amount = marketTrade.getAmount().doubleValue();
                totalAmount += amount;
                totalVolume += price * amount;
                priceLow = Math.min(priceLow, price);
                priceHigh = Math.max(priceHigh, price);
            }
            if (totalVolume > volumeFilter) {
                log.info(String.format("%s %s %s %s %.2f %.4f %.4f %.4f",
                                       first.getTs(), first.getTradeId(), first.getDirection(), list.size(), totalVolume, totalAmount, priceHigh, priceLow));
            }
        });
        Util.sleepSec(Integer.MAX_VALUE);
    }

}
