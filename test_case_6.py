from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_case_6():
    service = Service('C:\\Users\\Vaidehi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe')
    driver = webdriver.Chrome(service=service)
    driver.get('https://en.wikipedia.org/wiki/English_language')

    try:
        talk_link = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, 'ca-talk'))
        )
        talk_link.click()
        WebDriverWait(driver, 10).until(EC.title_contains('Talk:English language'))
        assert 'Talk:English language' in driver.title
        print('Test Case 6 Passed: Talk link is present and navigates correctly')
        driver.back()  # Return to the previous page
    except Exception as e:
        print(f'Test Case 6 Failed: {e}')

    driver.quit()
