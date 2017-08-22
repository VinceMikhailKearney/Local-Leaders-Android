v:
	vim app/src/main/java/industries/muskaqueers/thunderechosaber/Scripts/fetch_images.py

images:
	python app/src/main/java/industries/muskaqueers/thunderechosaber/Scripts/fetch_images.py http://127.0.0.1:8000/leaders/plainMlaData/

partyImages:
	python app/src/main/java/industries/muskaqueers/thunderechosaber/Scripts/fetchPartyImages.py http://127.0.0.1:8000/leaders/parties/
