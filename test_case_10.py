from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_case_10():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        wait = WebDriverWait(driver, 10)
        infobox_table = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, 'table.infobox.vevent.infobox-has-images-with-white-backgrounds')))
        assert infobox_table.is_displayed()
        print('Test Case 10 Passed: Infobox table with the specified class is present')
    except Exception as e:
        print(f'Test Case 10 Failed: {e}')

    driver.quit()
