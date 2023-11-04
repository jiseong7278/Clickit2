const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    // app.use(
    //     '/member',
    //     createProxyMiddleware({
    //         target: 'http://localhost:8080',
    //         changeOrigin: true,
    //     })
    // );
    // app.use(
    //     '/dormitory',
    //     createProxyMiddleware({
    //         target: 'http://localhost:8080',
    //         changeOrigin: true,
    //     })
    // );
    app.use(
        ['/dormitory', '/member'],
        createProxyMiddleware({
            target: 'http://localhost:8080',
            changeOrigin: true,
        })
    );
};