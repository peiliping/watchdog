java -jar target/watchdog-1.0-SNAPSHOT.jar \
	--logLevel debug \
	--ssl --host api.huobiasia.vip \
	--market hb \
	--heartBeat '{"pong":%s}' \
	--subscribe '{"req":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"sub":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"req":"market.btcusdt.kline.5min","symbol":"btcusdt","period":"5min"}${"sub":"market.btcusdt.kline.5min","symbol":"btcusdt","period":"5min"}${"req":"market.btcusdt.kline.15min","symbol":"btcusdt","period":"15min"}${"sub":"market.btcusdt.kline.15min","symbol":"btcusdt","period":"15min"}${"req":"market.btcusdt.kline.30min","symbol":"btcusdt","period":"30min"}${"sub":"market.btcusdt.kline.30min","symbol":"btcusdt","period":"30min"}${"req":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}${"sub":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}${"req":"market.btcusdt.kline.4hour","symbol":"btcusdt","period":"4hour"}${"sub":"market.btcusdt.kline.4hour","symbol":"btcusdt","period":"4hour"}'
