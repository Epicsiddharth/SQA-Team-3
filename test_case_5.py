from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_case_5():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        view_source_link = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, 'ca-viewsource'))
        )
        assert view_source_link is not None
        print('Test Case 5 Passed: View source link is present and clickable')
    except Exception as e:
        print(f'Test Case 5 Failed: {e}')

    driver.quit()
