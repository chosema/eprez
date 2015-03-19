var logger = require('./logger')

var express = require('express')
var http = require("http")
var cookieParser = require('cookie-parser')

var app = express()
app.use(cookieParser('SDFy9d7dfAHcfgBasUSd8sf4aa44d4fgd6dfgAS4ddAS4d86gsdS7SDfsd9ASaJ8DHh'))
app.use(express.static('WebContent'))
app.get('/stream/:token', function(req, res) {
    res.cookie('eprez_token', req.params.token, { signed: true });
    res.redirect('/');
})
app.get('/info', function(req, res) {
    var token = req.signedCookie.eprez_token
    // get presentation session by token and return json info
    res.json({
        sessionId: '',
        user: '',
        presenter: false,
        currentPageIndex: 0
    })
})

/* HTTP server initialization (with expressjs 'app' request handler) */
var httpServer = http.createServer(app).listen(3000)
var host = httpServer.address().address
var port = httpServer.address().port
logger.info('Web app listening at http://' + host + ':' + port)

module.exports = httpServer
