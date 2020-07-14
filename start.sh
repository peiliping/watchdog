java -jar target/watchdog-1.0-SNAPSHOT.jar \
	--logLevel info \
	--ssl --host api.huobiasia.vip \
	--market hb \
	--heartBeat '{"pong":%s}' \
	--subscribe '{"req":"market.lambusdt.kline.1min","symbol":"lambusdt","period":"1min"}${"sub":"market.lambusdt.kline.1min","symbol":"lambusdt","period":"1min"}${"req":"market.lambusdt.kline.60min","symbol":"lambusdt","period":"60min"}${"sub":"market.lambusdt.kline.60min","symbol":"lambusdt","period":"60min"}${"req":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"sub":"market.btcusdt.kline.1min","symbol":"btcusdt","period":"1min"}${"req":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}${"sub":"market.btcusdt.kline.60min","symbol":"btcusdt","period":"60min"}' \
	--marketConfig '{"candleShockRatioConditions":{"btcusdt":0.8,"lambusdt":1.5}}' \
	--barkIds xxxxx


