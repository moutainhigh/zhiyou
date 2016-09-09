import SimpleHTTPServer
import SocketServer
import re
import os
def htc(m):
    return chr(int(m.group(1),16))

def urldecode(url):
    rex=re.compile('%([0-9a-hA-H][0-9a-hA-H])',re.M)
    return rex.sub(htc,url)

class SETHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):

    def do_GET(self):
        print "GET"
        print self.headers;

    def do_POST(self):
        print "POST"
        print self.headers;
        # length = int(self.headers.getheader('content-length'))
        # qs = self.rfile.read(length)
        # url = urldecode(qs)
        # print url
        os.system('curl -u ufohjl:XabUCvOJv77fD5JN42EJr2Xfwd1hA http://jenkins.binggejia.com/view/zhiyou/job/zy-service-impl/build?token=token')
        os.system('curl -u ufohjl:XabUCvOJv77fD5JN42EJr2Xfwd1hA http://jenkins.binggejia.com/view/zhiyou/job/zy-admin/build?token=token')
        os.system('curl -u ufohjl:XabUCvOJv77fD5JN42EJr2Xfwd1hA http://jenkins.binggejia.com/view/zhiyou/job/zy-mobile/build?token=token')
        print 'sccess'
        self.wfile.write('hello world')


Handler = SETHandler
PORT = 6666
httpd = SocketServer.TCPServer(("0.0.0.0", PORT), Handler)
print "serving at port", PORT
httpd.serve_forever()