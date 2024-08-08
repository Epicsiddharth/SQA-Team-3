from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_case_11():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        wait = WebDriverWait(driver, 10)
        most_spoken_language_link = wait.until(EC.element_to_be_clickable((By.XPATH, '//a[@href="/wiki/List_of_languages_by_total_number_of_speakers" and @title="List of languages by total number of speakers"]')))
        print('Test Case 11 Passed: Most spoken language link is clickable')
    except Exception as e:
        print(f'Test Case 11 Failed: {e}')

    driver.quit()
