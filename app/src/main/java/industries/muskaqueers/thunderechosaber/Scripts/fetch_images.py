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
    f = opener.open(req)
    jsonResponse = json.loads(f.read())
    for i in jsonResponse:
        downloadImageFromUrl(i['MemberImgUrl'], i['MemberName'])

def downloadImageFromUrl(url, name):
    filename = "mla_with_id__%s" % (url.split('/')[-1]).replace("_s", "")
    print "Downloading image for -> %s" % name
    urllib.urlretrieve(url, filename)

'''
Was trying to make use of this to flag that we have a way of flagging
that we already downloaded the image for the MLA. Didn't get it working
YET. When we download it just overrides so not such a big deal.
'''
def mlaAlreadyDownloaded():
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
