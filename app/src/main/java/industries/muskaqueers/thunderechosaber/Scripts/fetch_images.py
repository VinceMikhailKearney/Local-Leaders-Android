#!/usr/bin/python

import urllib
import urllib2
import json
import os
import getpass

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def fetchJSONfile(str):
    os.chdir('app/src/main/res/drawable/')
    print "Changed dir to store images"
    req = urllib2.Request(str)
    opener = urllib2.build_opener()
    f = opener.open(req)
    jsonResponse = json.loads(f.read())
    for i in jsonResponse:
        downloadImageFromUrl(i['MemberImgUrl'], i['MemberName'])

def downloadImageFromUrl(url, name):
    filename = (url.split('/')[-1]).replace("_s", "")
    if not mlaAlreadyDownloaded(filename):
        print "Downloading image for -> %s" % name
        urllib.urlretrieve(url, filename)

def mlaAlreadyDownloaded(filename):
    for the_file in os.listdir('.'):
        file_path = os.path.join('.', the_file)
        try:
            if os.path.isfile(file_path):
                if filename in the_file:
                    return True
                return False
        except Exception as e:
            print(e)

# Don't use below but keeping if we need it
def clearOutMlaImageDirectory():
    folder = 'app/src/main/res/drawable/'
    for the_file in os.listdir(folder):
        file_path = os.path.join(folder, the_file)
        try:
            if os.path.isfile(file_path):
                os.unlink(file_path)
        except Exception as e:
            print(e)

if __name__ == "__main__":
    import sys
    fetchJSONfile(str(sys.argv[1]))
