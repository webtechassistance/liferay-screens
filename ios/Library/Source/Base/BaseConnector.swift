//
//  BaseConnector.swift
//  Liferay-iOS-Widgets-Swift-Sample
//
//  Created by jmWork on 28/09/14.
//  Copyright (c) 2014 Liferay. All rights reserved.
//

import UIKit


enum LiferayConnectorsQueue {

	static var queue: NSOperationQueue?

	static func addConnector(connector: BaseConnector) {
		if queue == nil {
			queue = NSOperationQueue()
			queue!.maxConcurrentOperationCount = 1
			queue!.qualityOfService = .UserInitiated
		}

		queue!.addOperation(connector)
	}

}


class BaseConnector: NSOperation {

	var lastError: NSError?
	var widget: BaseWidget

	internal var onComplete: (BaseConnector -> Void)?

	init(widget: BaseWidget) {
		self.widget = widget

		super.init()
	}

	func addToQueue(onComplete: BaseConnector -> Void) {
		self.onComplete = onComplete

		LiferayConnectorsQueue.addConnector(self)
	}

	internal override func main() {
		if preRun() {
			doRun()
			postRun()

			if self.onComplete != nil {
				dispatch_async(dispatch_get_main_queue()) {
					self.onComplete!(self)
				}
			}
		}
	}

	internal func preRun() -> Bool {
		return false
	}

	internal func doRun() {
	}

	internal func postRun() {
	}

	internal func showHUD(#message: String, details: String? = nil) {
		dispatch_async(dispatch_get_main_queue()) {
			self.widget.startOperationWithMessage(message, details: details)
		}
	}

	internal func hideHUD() {
		dispatch_async(dispatch_get_main_queue()) {
			self.widget.finishOperation()
		}
	}

	internal func hideHUD(#error: NSError, message: String, details: String? = nil) {
		dispatch_async(dispatch_get_main_queue()) {
			self.widget.finishOperationWithError(error, message: message, details: details)
		}
	}

}