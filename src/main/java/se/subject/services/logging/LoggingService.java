/*
    Subject
    Copyright (C) 2020 Clifford Carnmo

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package se.subject.services.logging;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService implements ILoggingService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void Log(HashMap<String, Object> payload) {
		if (payload.containsKey("class")) {
			logger.info("[Subject] Class: {}", payload.get("class"));
		}

		if (payload.containsKey("session")) {
			HttpSession session = (HttpSession)payload.get("session");
			logger.info("[Subject] User: {}", session.getAttribute("user"));
		}

		if (payload.containsKey("model")) {
			logger.info("[Subject] Model: {}", payload.get("model"));
		}
	}
}