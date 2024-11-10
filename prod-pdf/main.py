from fastapi import FastAPI, Request
from fastapi.responses import StreamingResponse
from jinja2 import Environment, FileSystemLoader
from weasyprint import HTML
import io

# To run - uvicorn generate:app --reload

app = FastAPI()


@app.post("/api")
async def create_report(request: Request):
    data = await request.json()
    print(data)

    env = Environment(loader=FileSystemLoader('.'))



    template = env.get_template('template.html')

    rendered_html = template.render(
        advisor_details=data["advisorDetails"],
        client_details=data["clientDetails"],
        subscription_details=data["subscriptionDetails"],
        risk_profiling=data["riskProfiling"]

    )

    pdf_buffer = io.BytesIO()
    HTML(string=rendered_html, base_url='base_url').write_pdf(pdf_buffer)
    pdf_buffer.seek(0)

    return StreamingResponse(pdf_buffer, media_type="application/pdf",
                             headers={"Content-Disposition": "attachment; filename=report.pdf"})
