SUML - Application Android

Application Android dont le but est de récolter les données de géolocalisation de l'utilisateur pour les envoyer à la plateforme centralisé, permettant d'allumer les lampadaires connectés à proximité.
L'application utilise un foreground service pour lancer un job service de localisation (LocationJobService) toutes les 5 secondes.
Une fois la localisation obtenue la donnée est envoyée via une requête http vers la plateforme centrale (classe RequestInterface).