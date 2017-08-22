#!/usr/bin/python

import urllib
import urllib2
import json
import os

def fetchJSONfile(str):
    os.chdir('app/src/main/res/drawable/')
    print "Changed dir to store images"
    req = urllib2.Request(str)
    opener = urllib2.build_opener()
    file = opener.open(req)
    jsonResponse = json.loads(file.read())
    jsonArray = jsonResponse['response']
    for i in jsonArray:
        downloadImageFromUrl(i['image_url'], i['name'])

def downloadImageFromUrl(url, name):
    filename = "party_with_id__%s.jpg" % name.lower()
    print "Downloading image for -> %s" % name
    urllib.urlretrieve(url, filename)

if __name__ == "__main__":
    import sys
    fetchJSONfile(str(sys.argv[1]))
