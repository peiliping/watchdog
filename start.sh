java -jar target/watchdog-1.0-SNAPSHOT.jar \
	--logLevel debug \
	--ssl --host api.huobiasia.vip \
	--market hb \
	--heartBeat '{"pong":%s}' \
	--subscribe '{"req":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"sub":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"req":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}${"sub":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}${"req":"market.btcusdt.kline.1day","symbol":"btcusdt","period":"1day"}${"sub":"market.btcusdt.kline.1day","symbol":"btcusdt","period":"1day"}'
