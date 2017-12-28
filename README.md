- [x] Cikel zvezne integracije(Travis CI ustvari sliko Docker in objavi na Docker Hub)
- [x] Konfiguracija(identificiranje konfiguracije, ločitev konfiguracije in implementacije
  (spreminjanje konfiguracije brez ponovnega nameščanja), konfiguracijske datoteke, 
  okoljske spremenljivke, **konfiguracijski strežnik)**
- [ ] Odkrivanje storitev s pomočjo etcd ali Consul..
- [ ] Kubernetes (konfigurirajte različne service (ClusterID, NodePort)
- [ ] HealthCheck (vsaj dve kontroli zdravja, API za preverjanje zdravja vrača ustrezne HTTP status kode, 
  da bo skladen s Kubernetes Liveness Probes, v Kubernetes Deployments dodajte Liveness Probes, 
  simulirajte bolno storitev in opazujte ali jo Kubernetes zažene na novo) 
- [ ] Metrics (storitvam dodajte končno točko na kateri izpostavite nekaj metrik)
- [x] Dnevniške datoteke (pošiljanje logov na logit.io, dodajanje konteksta logom(ime storitve, verzija, okolje…),
  beležite vhode iz izhode v storitve, **pripravite tri zanimive poizvedbe dnevnikov 
  (npr. izpis vseh vstopov v določeno metodo)**
- [ ] Izolacija in toleranca napak (simulirajte napako na enem izmed klicev med dvema mikrostoritvama 
  npr. mikrostoritev ni dosegljiva), pripravite ustrezen nadomestni mehanizem (fallback), 
  ki se proži v primeru napake na klicani mikrostoritvi), pri implementaciji odpornosti na 
  napake uporabite časovni zamik (timeout) in ponovno simulirajte napako na eni izmed mikrostoritev 
  in opazujte delovanje aplikacije)
