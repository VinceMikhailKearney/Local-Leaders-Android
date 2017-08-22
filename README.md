# ThunderEchoSabre

Thunder Echo Sabre is a tool that allows people to connect to their local MLAs to voice any
concerns they may have. By combining open soure data we hope to build a go-to app that will
save people the bother of finding out their constituency, working out who their MLAs are and figuring out
how to contact them. We hope we can do it all for you.

This app uses a Django/Python server to get the MLA and Parties information, linked here: https://github.com/VinceMikhailKearney/DevelopmentServer

- Python scripts are used to download and place the images for each of the MLAs in the Drawable folder, same goes for the party images.
- Database was originally written using SQLite and then was changed to GreenDAO.
- The server was originally Firebase before changing to use our own Django server.
