# lab-results-ingest-service

Service Spring Boot separe pour:
- recevoir les donnees d'analyse biologique,
- uploader le PDF vers DigitalOcean Spaces,
- sauvegarder les donnees dans MySQL avec l'URL du PDF retournee.

## Endpoint

- `POST /api/v1/lab-reports/ingest`
- `GET /api/v1/lab-reports?enterprise_code=CNO`
- `Content-Type: application/json`

### Payload attendu

Le payload contient les champs obligatoires `enterprise_code` et `pdf_bytes_base64`.

```json
{
  "patient": {
    "patient_id": "98765",
    "last_name": "BENALI",
    "first_name": "SARA",
    "birth_date": "19920315",
    "sex": "F"
  },
  "results": [
    {
      "test_code": "GLUC",
      "test_name": "Glycemie a jeun",
      "value": "1.28",
      "unit": "g/l",
      "reference_range": "0.70-1.10",
      "abnormal_flag": "H",
      "test_type": "BIOCHIMIE"
    }
  ],
  "pdf_path": "/app/pdfs/resultat_98765_20260224155320.pdf",
  "pdf_url": "http://localhost:8000/static/pdfs/resultat_98765_20260224155320.pdf",
  "report_id": 1,
  "enterprise_code": "CNO",
  "pdf_bytes_base64": "JVBERi0xLjcKJc..."
}
```

### Reponse

```json
{
  "id": 10,
  "reportId": 1,
  "enterpriseCode": "CNO",
  "patientId": "98765",
  "pdfUrl": "https://yata-medias.fra1.digitaloceanspaces.com/lab-reports/98765/resultat_1_20260224155320_uuid.pdf",
  "createdAt": "2026-02-24T15:53:20Z"
}
```

### Filtrer les rapports par entreprise

`GET /api/v1/lab-reports?enterprise_code=CNO`

Renvoie la liste des rapports de l'entreprise, triée du plus recent au plus ancien.

## Configuration

Dans `src/main/resources/application.properties`:
- MySQL: `spring.datasource.*`
- DigitalOcean Spaces (meme noms que dans projet existant):
  - `SPACE_KEY`
  - `SPACE_SECRET`
  - `SPACE_ENDPOINT`
  - `SPACE_REGION`
  - `SPACE_BUCKET`

## Lancement

```bash
mvn spring-boot:run
```
