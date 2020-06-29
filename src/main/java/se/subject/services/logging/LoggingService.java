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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoggingService implements ILoggingService {
	@Value("${subject.logging.enabled}")
	private Boolean loggingEnabled;

	private List<LoggingEvent> loggingEvents = new ArrayList<LoggingEvent>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void Init() {
		this.loggingEvents.clear();
	}

	public void AddEvent(LoggingEvent loggingEvent) {
		this.loggingEvents.add(loggingEvent);
	}

	public void Log() {
		if (loggingEnabled) {
			for (LoggingEvent loggingEvent : this.loggingEvents) {
				logger.info("[Subject] {}: {}", loggingEvent.getTitle(), loggingEvent.getContent());
			}
		}
	}
}