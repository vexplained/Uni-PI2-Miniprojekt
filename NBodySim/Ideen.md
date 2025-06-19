# Ideen
- Help Menu: Tools, Funktionen knapp erklären (Text reicht, **keine Bilder** -> Aufwand sparen)
- Menubar:
    - "Massen hinzufügen" Tool:
        - mit Toggle
        - Masse einstellbar -> Radius des Kreises verändert sich; z. B. über Slider in Sidebar UND Mausrad, während auf
            Canvas
        - Mit Anfangsgeschwindigkeit -> Möglichkeit, anstatt nur auf den Canvas zu klicken, auf den Canvas klicken und
            ziehen, um Startposition und Startgeschwindigkeit zu definieren
    - "Bewegen/Move" Tool:
        - Canvas bewegen & zoomen
    - s. Features: Traces, Play/Pause, v-Vektoren

- Features
    - Traces
        - Punkte oder Linien?
        - Werden auf separaten Layern gezeichnet: Wenn N letzte Positionen jedes Punktes festgehalten werden, gibt es N
            BufferedImages, die alle übereinandergelegt werden; (vermutlich) besser für Performance als jeden Tick
            eine gigantische Anzahl an individuellen Kreisen/Linien zu zeichnen. Jeden Tick wird das älteste Image
            geleert. Z. B. so: https://stackoverflow.com/a/35984161
        - Falls noch Zeit: (Farbe/)Opacity einstellbar -> Achtung beim Layern: evtl. alle BufferedImages kombinieren und
            anschließend Opacity setzen?
    - Play/Pause
        - Simulation soll angehalten werden; Canvas weiterhin pan- & zoombar
    - Geschwindigkeitsvektoren anzeigen
    - Alternativ: Farbe in Abhängigkeit von Geschwindigkeit
        \-> wenn noch Zeit: Legende am Rand anzeigen
    - (Collapse bodies: wenn Distanz < epsilon, zwei Körper vereinen) -> Ressourcenintensiv: O(n^2) ohne Spatial Hashing
        wegen Distanzberechnung
    - Gravitationskonstante einstellbar
    - Simulationsgeschwindigkeit / Step einstellbar
        -> wenn noch Zeit: hier ggf. noch zusätzliche Kalkulationen für Kollisionstests: zwischen 2 Positionen jedes
        Punktes wird (endliche) Gerade gezogen; Intersection Test zwischen den Strecken aller Punkte
        -> falls Intersection Test positiv => collapse bodies

# Prioritäten
1. CanvasController & grundlegende Grafikerzeugung ausschließlich mit Massenpunkt-Rendering. Zunächst vordefinierte
    Punkte; an DynamicCanvas orientieren?
2. Simulation ohne collision checking
3. Interaktivität
    - UI bauen
    - Körper hinzufügen
    - Kamera bewegen