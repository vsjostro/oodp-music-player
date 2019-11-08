#!/usr/bin/python
import sys
import lyricsgenius

song = sys.argv[1]
artist = sys.argv[2]
client_access_token = sys.argv[3]

genius = lyricsgenius.Genius(client_access_token)
song = genius.search_song(song, artist)
print(song.lyrics)