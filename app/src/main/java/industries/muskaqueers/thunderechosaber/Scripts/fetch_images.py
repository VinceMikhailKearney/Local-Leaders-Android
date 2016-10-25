#!/usr/bin/python

import urllib
import urllib2
import json
import os
import getpass

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def fetchJSONfile(str):
    clearOutMlaImageDirectory()
    print "Cleared out existing directory. Going to update."
    os.chdir('app/src/main/res/drawable/mlas')
    print "Changed dir to store images"
    req = urllib2.Request(str)
    opener = urllib2.build_opener()
    f = opener.open(req)
    jsonResponse = json.loads(f.read())
    for i in jsonResponse:
        print "Downloading image for -> %s" % i['MemberName']
        downloadImageFromUrl(i['MemberImgUrl'])

def downloadImageFromUrl(str):
    urlLink = str
    filename = (urlLink.split('/')[-1]).replace("_s", "")
    urllib.urlretrieve(urlLink, filename)

def clearOutMlaImageDirectory():
    folder = 'app/src/main/res/drawable/mlas'
    for the_file in os.listdir(folder):
        print the_file
        file_path = os.path.join(folder, the_file)
        print file_path
        try:
            if os.path.isfile(file_path):
                os.unlink(file_path)
        except Exception as e:
            print(e)

if __name__ == "__main__":
    import sys
    fetchJSONfile(str(sys.argv[1]))
