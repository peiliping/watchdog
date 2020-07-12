java -jar ../target/watchdog-1.0-SNAPSHOT.jar \
	--logLevel info \
	--ssl --host api.huobiasia.vip \
	--market hb \
	--heartBeat '{"pong":%s}' \
	--subscribe '{"req":"market.lambusdt.kline.30min","symbol":"lambusdt","period":"30min"}${"sub":"market.lambusdt.kline.30min","symbol":"lambusdt","period":"30min"}' \
	--barkIds XXXXX